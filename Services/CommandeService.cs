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
    Console.WriteLine($"=== DÉBUT CRÉATION COMMANDE SERVICE ===");
    Console.WriteLine($"ClientId: {clientId}");
    Console.WriteLine($"TypeLivraison: {model.TypeLivraison}");
    
    // ✅ FIX: Utilisez l'ExecutionStrategy pour gérer les transactions
    var executionStrategy = _context.Database.CreateExecutionStrategy();
    
    return await executionStrategy.ExecuteAsync(async () =>
    {
        using var transaction = await _context.Database.BeginTransactionAsync();
        
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
                // ⭐ IMPORTANT: Utilisez exactement "EN_ATTENTE" 
                Etat = "EN_ATTENTE",
                Payee = false,
                Archived = false,
                CreatedAt = DateTime.UtcNow,
                UpdatedAt = DateTime.UtcNow,
                Total = 0 // Initialiser à 0, on calculera après
            };

            Console.WriteLine($"✅ Commande object créé, Etat: {commande.Etat}");

            _context.Commandes.Add(commande);
            
            // Sauvegarder d'abord pour obtenir l'ID
            Console.WriteLine("⏳ Sauvegarde de la commande...");
            await _context.SaveChangesAsync();
            Console.WriteLine($"✅ Commande sauvegardée avec ID: {commande.Id}");

            // Add burger or menu item
            if (model.BurgerId.HasValue)
            {
                Console.WriteLine($"⏳ Recherche burger ID: {model.BurgerId.Value}");
                var burger = await _context.Burgers
                    .Where(b => b.Id == model.BurgerId.Value && !b.Archived)
                    .FirstOrDefaultAsync();
                
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
                    Console.WriteLine($"✅ Burger ajouté: {burger.Nom} - Prix: {burger.Prix}");
                }
                else
                {
                    Console.WriteLine($"❌ Burger non trouvé: {model.BurgerId.Value}");
                }
            }
            else if (model.MenuId.HasValue)
            {
                Console.WriteLine($"⏳ Recherche menu ID: {model.MenuId.Value}");
                var menu = await _context.Menus
                    .Where(m => m.Id == model.MenuId.Value && !m.Archived)
                    .FirstOrDefaultAsync();
                
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
                    Console.WriteLine($"✅ Menu ajouté: {menu.Nom} - Prix: {menu.Prix}");
                }
                else
                {
                    Console.WriteLine($"❌ Menu non trouvé: {model.MenuId.Value}");
                }
            }

            // Add complements
            if (model.ComplementIds != null && model.ComplementIds.Any())
            {
                Console.WriteLine($"⏳ Ajout de {model.ComplementIds.Count} complément(s)");
                foreach (var complementId in model.ComplementIds)
                {
                    var complement = await _context.Complements
                        .Where(c => c.Id == complementId && !c.Archived)
                        .FirstOrDefaultAsync();
                    
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
                        Console.WriteLine($"✅ Complement ajouté: {complement.Nom} - Prix: {complement.Prix}");
                    }
                }
            }

            // Add delivery cost if applicable
            if (model.TypeLivraison == "LIVRAISON" && model.ZoneId.HasValue)
            {
                Console.WriteLine($"⏳ Recherche zone ID: {model.ZoneId.Value}");
                var zone = await _context.Zones
                    .Where(z => z.Id == model.ZoneId.Value && !z.Archived)
                    .FirstOrDefaultAsync();
                
                if (zone != null)
                {
                    total += zone.PrixLivraison;
                    Console.WriteLine($"✅ Frais de livraison ajoutés: {zone.PrixLivraison} pour zone {zone.Nom}");
                }
            }

            // Mettre à jour le total
            commande.Total = total;
            commande.UpdatedAt = DateTime.UtcNow;
            
            Console.WriteLine($"✅ Total final de la commande: {total}");
            
            // Sauvegarder les modifications
            await _context.SaveChangesAsync();
            
            // Valider la transaction
            await transaction.CommitAsync();
            
            Console.WriteLine($"✅ Commande {commande.Id} créée avec succès!");
            
            return commande;
        }
        catch (Exception ex)
        {
            await transaction.RollbackAsync();
            Console.WriteLine($"❌ ERREUR CRITIQUE dans CreateCommandeAsync:");
            Console.WriteLine($"Message: {ex.Message}");
            Console.WriteLine($"StackTrace: {ex.StackTrace}");
            
            if (ex.InnerException != null)
            {
                Console.WriteLine($"Inner Exception: {ex.InnerException.Message}");
            }
            
            throw;
        }
    });
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
                Etat = c.Etat, // Déjà string, pas besoin de .ToString()
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
                Etat = commande.Etat,
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

            // ⭐⭐ Comparez avec "EN_ATTENTE" (string) au lieu de EtatCommande.EN_ATTENTE (enum) ⭐⭐
            if (commande == null || commande.Etat != "EN_ATTENTE")
                return false;

            // ⭐⭐ Utilisez "ANNULEE" (string) au lieu de EtatCommande.ANNULEE (enum) ⭐⭐
            commande.Etat = "ANNULEE";
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

        public async Task<Models.Commande?> GetCommandeByIdAsync(int commandeId, int clientId)
{
    return await _context.Commandes
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
}
    }
}