using Microsoft.EntityFrameworkCore;
using BrasilBurger.Data;
using BrasilBurger.Models;
using BrasilBurger.ViewModels;

namespace BrasilBurger.Services
{
    public class CommandeService
    {
        private readonly ApplicationDbContext _context;

        public CommandeService(ApplicationDbContext context)
        {
            _context = context;
        }

        public async Task<Commande> CreateCommandeAsync(int clientId, CreateCommandeViewModel model)
        {
            try
            {
                decimal total = 0;
                var commande = new Commande
                {
                    ClientId = clientId,
                    TypeLivraison = model.TypeLivraison,
                    DateCommande = DateTime.UtcNow,
                    ZoneId = model.ZoneId,
                    AdresseLivraison = model.AdresseLivraison,
                    Etat = EtatCommande.EN_ATTENTE, // ← ENUM
                    Payee = false,
                    Archived = false,
                    CreatedAt = DateTime.UtcNow,
                    UpdatedAt = DateTime.UtcNow
                };

                _context.Commandes.Add(commande);
                await _context.SaveChangesAsync();

                // Add burger or menu item
                if (model.BurgerId.HasValue)
                {
                    var burger = await _context.Burgers.FindAsync(model.BurgerId.Value);
                    if (burger != null)
                    {
                        var item = new CommandeItem
                        {
                            CommandeId = commande.Id,
                            BurgerId = burger.Id,
                            Quantite = model.Quantite,
                            PrixUnitaire = burger.Prix
                        };
                        _context.CommandeItems.Add(item);
                        total += burger.Prix * model.Quantite;
                    }
                }
                else if (model.MenuId.HasValue)
                {
                    var menu = await _context.Menus.FindAsync(model.MenuId.Value);
                    if (menu != null)
                    {
                        var item = new CommandeItem
                        {
                            CommandeId = commande.Id,
                            MenuId = menu.Id,
                            Quantite = model.Quantite,
                            PrixUnitaire = menu.Prix
                        };
                        _context.CommandeItems.Add(item);
                        total += menu.Prix * model.Quantite;
                    }
                }

                // Add complements as separate CommandeItems
                foreach (var complementId in model.ComplementIds ?? new List<int>())
                {
                    var complement = await _context.Complements.FindAsync(complementId);
                    if (complement != null)
                    {
                        var complementItem = new CommandeItem
                        {
                            CommandeId = commande.Id,
                            ComplementId = complement.Id,
                            Quantite = 1,
                            PrixUnitaire = complement.Prix
                        };
                        _context.CommandeItems.Add(complementItem);
                        total += complement.Prix;
                    }
                }

                // Add delivery cost if applicable
                if (model.TypeLivraison == "LIVRAISON" && model.ZoneId.HasValue)
                {
                    var zone = await _context.Zones.FindAsync(model.ZoneId.Value);
                    if (zone != null)
                    {
                        total += zone.PrixLivraison;
                    }
                }

                commande.Total = total;
                commande.UpdatedAt = DateTime.UtcNow;
                await _context.SaveChangesAsync();

                return commande;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Erreur création commande: {ex.Message}");
                throw;
            }
        }

        public async Task<List<CommandeViewModel>> GetClientCommandesAsync(int clientId)
        {
            var commandes = await _context.Commandes
                .Where(c => c.ClientId == clientId && !c.Archived)
                .OrderByDescending(c => c.DateCommande)
                .Include(c => c.CommandeItems!)
                    .ThenInclude(ci => ci.Burger)
                .Include(c => c.CommandeItems!)
                    .ThenInclude(ci => ci.Menu)
                .Include(c => c.CommandeItems!)
                    .ThenInclude(ci => ci.Complement)
                .ToListAsync();

            return commandes.Select(c => new CommandeViewModel
            {
                Id = c.Id,
                DateCommande = c.DateCommande,
                Etat = c.Etat.ToString(), // ← .ToString() POUR CONVERTIR ENUM → STRING
                TypeLivraison = c.TypeLivraison,
                Total = c.Total,
                Payee = c.Payee,
                AdresseLivraison = c.AdresseLivraison ?? string.Empty,
                Items = c.CommandeItems != null ? c.CommandeItems.Select(ci => new CommandeItemViewModel
                {
                    Type = ci.BurgerId != null ? "Burger" : 
                           ci.MenuId != null ? "Menu" : "Complement",
                    Nom = ci.BurgerId != null ? ci.Burger!.Nom : 
                          ci.MenuId != null ? ci.Menu!.Nom : 
                          ci.Complement!.Nom,
                    Quantite = ci.Quantite,
                    PrixUnitaire = ci.PrixUnitaire
                }).ToList() : new List<CommandeItemViewModel>()
            }).ToList();
        }

        public async Task<CommandeViewModel?> GetCommandeDetailAsync(int commandeId, int clientId)
        {
            var commande = await _context.Commandes
                .Where(c => c.Id == commandeId && c.ClientId == clientId && !c.Archived)
                .Include(c => c.CommandeItems!)
                    .ThenInclude(ci => ci.Burger)
                .Include(c => c.CommandeItems!)
                    .ThenInclude(ci => ci.Menu)
                .Include(c => c.CommandeItems!)
                    .ThenInclude(ci => ci.Complement)
                .Include(c => c.Zone)
                .Include(c => c.Livreur)
                .FirstOrDefaultAsync();

            if (commande == null)
                return null;

            return new CommandeViewModel
            {
                Id = commande.Id,
                DateCommande = commande.DateCommande,
                Etat = commande.Etat.ToString(), // ← .ToString() POUR CONVERTIR ENUM → STRING
                TypeLivraison = commande.TypeLivraison,
                Total = commande.Total,
                Payee = commande.Payee,
                AdresseLivraison = commande.AdresseLivraison ?? string.Empty,
                Items = commande.CommandeItems != null ? commande.CommandeItems.Select(ci => new CommandeItemViewModel
                {
                    Type = ci.BurgerId != null ? "Burger" : 
                           ci.MenuId != null ? "Menu" : "Complement",
                    Nom = ci.BurgerId != null ? ci.Burger!.Nom : 
                          ci.MenuId != null ? ci.Menu!.Nom : 
                          ci.Complement!.Nom,
                    Quantite = ci.Quantite,
                    PrixUnitaire = ci.PrixUnitaire
                }).ToList() : new List<CommandeItemViewModel>()
            };
        }

        public async Task<bool> CancelCommandeAsync(int commandeId, int clientId)
        {
            var commande = await _context.Commandes
                .FirstOrDefaultAsync(c => c.Id == commandeId && c.ClientId == clientId);

            if (commande == null || commande.Etat != EtatCommande.EN_ATTENTE) // ← COMPARE AVEC ENUM
                return false;

            commande.Etat = EtatCommande.ANNULEE; // ← UTILISEZ ENUM
            commande.UpdatedAt = DateTime.UtcNow;
            await _context.SaveChangesAsync();

            return true;
        }

        public async Task<List<Zone>> GetZonesAsync()
        {
            return await _context.Zones
                .Where(z => !z.Archived)
                .ToListAsync();
        }
    }
}