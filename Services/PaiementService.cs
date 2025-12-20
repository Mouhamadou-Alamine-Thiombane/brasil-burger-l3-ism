using Microsoft.EntityFrameworkCore;
using BrasilBurger.Data;
using BrasilBurger.Models;
using BrasilBurger.ViewModels;

namespace BrasilBurger.Services
{
    public class PaiementService
    {
        private readonly ApplicationDbContext _context;

        public PaiementService(ApplicationDbContext context)
        {
            _context = context;
        }

        public async Task<Paiement> ProcessPaiementAsync(PaiementViewModel model)
{
    // Pour Neon PostgreSQL avec retry
    var executionStrategy = _context.Database.CreateExecutionStrategy();
    
    return await executionStrategy.ExecuteAsync(async () =>
    {
        using var transaction = await _context.Database.BeginTransactionAsync();

        try
        {
            var commande = await _context.Commandes
                .FirstOrDefaultAsync(c => c.Id == model.CommandeId);

            if (commande == null)
                throw new Exception("Commande non trouvée");

            if (commande.Payee)
                throw new Exception("Cette commande a déjà été payée");

            var paiement = new Paiement
            {
                CommandeId = model.CommandeId,
                Montant = model.Montant,
                Methode = model.Methode,
                Reference = model.Reference,
                DatePaiement = DateTime.Now,
                Archived = false,
                CreatedAt = DateTime.Now,
                UpdatedAt = DateTime.Now
            };

            _context.Paiements.Add(paiement);

            // Update commande status
            commande.Payee = true;
            commande.Etat = EtatCommande.VALIDEE;
            commande.UpdatedAt = DateTime.UtcNow;

            await _context.SaveChangesAsync();
            await transaction.CommitAsync();

            return paiement;
        }
        catch (Exception)
        {
            await transaction.RollbackAsync();
            throw;
        }
    });
}

        public async Task<PaiementConfirmationViewModel?> GetPaiementConfirmationAsync(int paiementId)
        {
            return await _context.Paiements
                .Where(p => p.Id == paiementId)
                .Include(p => p.Commande)
                .Select(p => new PaiementConfirmationViewModel
                {
                    PaiementId = p.Id,
                    CommandeId = p.CommandeId,
                    Montant = p.Montant,
                    Methode = p.Methode,
                    DatePaiement = p.DatePaiement,
                    Reference = p.Reference
                })
                .FirstOrDefaultAsync();
        }

        public async Task<bool> IsCommandePayeeAsync(int commandeId)
        {
            return await _context.Paiements
                .AnyAsync(p => p.CommandeId == commandeId);
        }

        public async Task<Paiement?> GetPaiementByCommandeIdAsync(int commandeId)
        {
            return await _context.Paiements
                .FirstOrDefaultAsync(p => p.CommandeId == commandeId);
        }
    }
}