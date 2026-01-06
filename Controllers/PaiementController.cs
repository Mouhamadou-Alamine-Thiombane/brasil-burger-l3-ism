using Microsoft.AspNetCore.Mvc;
using BrasilBurger.Services;
using BrasilBurger.ViewModels;

namespace BrasilBurger.Controllers
{
    public class PaiementController : Controller
    {
        private readonly PaiementService _paiementService;
        private readonly SessionService _sessionService;
        private readonly CommandeService _commandeService;

        public PaiementController(
            PaiementService paiementService,
            SessionService sessionService,
            CommandeService commandeService)
        {
            _paiementService = paiementService;
            _sessionService = sessionService;
            _commandeService = commandeService;
        }

        [HttpGet]
        public async Task<IActionResult> Paiement(int commandeId)
        {
            if (!_sessionService.IsAuthenticated())
            {
                return RedirectToAction("Login", "Auth");
            }

            var clientId = _sessionService.GetClientId();
            if (!clientId.HasValue)
            {
                return RedirectToAction("Login", "Auth");
            }

            var commande = await _commandeService.GetCommandeDetailAsync(commandeId, clientId.Value);

            if (commande == null)
            {
                return NotFound();
            }

            if (commande.Payee)
            {
                TempData["ErrorMessage"] = "Cette commande a déjà été payée";
                return RedirectToAction("Detail", "Commande", new { id = commandeId });
            }

            var model = new PaiementViewModel
            {
                CommandeId = commandeId,
                Montant = commande.Total
            };

            ViewBag.IsAuthenticated = true;
            ViewBag.ClientNom = _sessionService.GetClientNom() ?? string.Empty;
            ViewBag.Commande = commande;
            return View(model);
        }

        [HttpPost]
        public async Task<IActionResult> ProcessPaiement(PaiementViewModel model)
        {
            if (!_sessionService.IsAuthenticated())
            {
                return RedirectToAction("Login", "Auth");
            }

            var clientId = _sessionService.GetClientId();
            if (!clientId.HasValue)
            {
                return RedirectToAction("Login", "Auth");
            }

            if (!ModelState.IsValid)
            {
                var commande = await _commandeService.GetCommandeDetailAsync(model.CommandeId, clientId.Value);
                ViewBag.IsAuthenticated = true;
                ViewBag.ClientNom = _sessionService.GetClientNom() ?? string.Empty;
                ViewBag.Commande = commande;
                return View("Paiement", model);
            }

            try
            {
                var paiement = await _paiementService.ProcessPaiementAsync(model);
                return RedirectToAction("Confirmation", new { paiementId = paiement.Id });
            }
            catch (Exception ex)
            {
                ModelState.AddModelError(string.Empty, ex.Message);
                var commande = await _commandeService.GetCommandeDetailAsync(model.CommandeId, clientId.Value);
                ViewBag.IsAuthenticated = true;
                ViewBag.ClientNom = _sessionService.GetClientNom() ?? string.Empty;
                ViewBag.Commande = commande;
                return View("Paiement", model);
            }
        }

        [HttpGet]
public async Task<IActionResult> Confirmation(int paiementId)
{
    Console.WriteLine($"=== CONFIRMATION PAIEMENT ID: {paiementId} ===");
    
    if (!_sessionService.IsAuthenticated())
    {
        return RedirectToAction("Login", "Auth");
    }

    var paiement = await _paiementService.GetPaiementByIdAsync(paiementId);
    
    if (paiement == null)
    {
        Console.WriteLine($"❌ Paiement {paiementId} non trouvé");
        return NotFound();
    }

    var clientId = _sessionService.GetClientId();
    if (!clientId.HasValue)
    {
        return RedirectToAction("Login", "Auth");
    }

    // ✅ Utilisez la nouvelle méthode qui retourne un Commande (modèle EF)
    var commande = await _commandeService.GetCommandeByIdAsync(paiement.CommandeId, clientId.Value);
    
    if (commande == null)
    {
        Console.WriteLine($"❌ Commande {paiement.CommandeId} non trouvée");
        return NotFound();
    }

    ViewBag.Paiement = paiement;
    ViewBag.IsAuthenticated = true;
    ViewBag.ClientNom = _sessionService.GetClientNom() ?? string.Empty;
    
    return View(commande);
}
    }
}