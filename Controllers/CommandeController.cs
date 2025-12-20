using Microsoft.AspNetCore.Mvc;
using BrasilBurger.Services;
using BrasilBurger.ViewModels;

namespace BrasilBurger.Controllers
{
    public class CommandeController : Controller
    {
        private readonly CommandeService _commandeService;
        private readonly SessionService _sessionService;
        private readonly BurgerService _burgerService;

        public CommandeController(
            CommandeService commandeService, 
            SessionService sessionService,
            BurgerService burgerService)
        {
            _commandeService = commandeService;
            _sessionService = sessionService;
            _burgerService = burgerService;
        }

        [HttpPost]
        public async Task<IActionResult> Create(CreateCommandeViewModel model)
        {
            if (!_sessionService.IsAuthenticated())
            {
                return RedirectToAction("Login", "Auth");
            }

            if (!ModelState.IsValid)
            {
                // Return to appropriate page with errors
                if (model.BurgerId.HasValue)
                {
                    var burger = await _burgerService.GetBurgerDetailAsync(model.BurgerId.Value);
                    if (burger != null)
                    {
                        ViewBag.Complements = await _burgerService.GetComplementsAsync();
                        ViewBag.IsAuthenticated = true;
                        return View("~/Views/Burger/Commander.cshtml", model);
                    }
                }
                else if (model.MenuId.HasValue)
                {
                    var menu = await _burgerService.GetMenuDetailAsync(model.MenuId.Value);
                    if (menu != null)
                    {
                        ViewBag.IsAuthenticated = true;
                        return View("~/Views/Burger/Commander.cshtml", model);
                    }
                }

                return RedirectToAction("Index", "Home");
            }

            var clientId = _sessionService.GetClientId();
            if (!clientId.HasValue)
            {
                return RedirectToAction("Login", "Auth");
            }
            
            var commande = await _commandeService.CreateCommandeAsync(clientId.Value, model);

            return RedirectToAction("Paiement", "Paiement", new { commandeId = commande.Id });
        }

        [HttpGet]
        public async Task<IActionResult> MesCommandes()
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
            
            var commandes = await _commandeService.GetClientCommandesAsync(clientId.Value);

            ViewBag.IsAuthenticated = true;
            ViewBag.ClientNom = _sessionService.GetClientNom() ?? string.Empty;
            return View(commandes);
        }

        [HttpGet]
        public async Task<IActionResult> Detail(int id)
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
            
            var commande = await _commandeService.GetCommandeDetailAsync(id, clientId.Value);

            if (commande == null)
            {
                return NotFound();
            }

            ViewBag.IsAuthenticated = true;
            ViewBag.ClientNom = _sessionService.GetClientNom() ?? string.Empty;
            return View(commande);
        }

        [HttpPost]
        public async Task<IActionResult> Annuler(int id)
        {
            if (!_sessionService.IsAuthenticated())
            {
                return Json(new { success = false, message = "Non authentifié" });
            }

            var clientId = _sessionService.GetClientId();
            if (!clientId.HasValue)
            {
                return Json(new { success = false, message = "Session invalide" });
            }
            
            var success = await _commandeService.CancelCommandeAsync(id, clientId.Value);

            if (success)
            {
                return Json(new { success = true, message = "Commande annulée avec succès" });
            }

            return Json(new { success = false, message = "Impossible d'annuler cette commande" });
        }

        [HttpGet]
        public async Task<IActionResult> GetZones()
        {
            var zones = await _commandeService.GetZonesAsync();
            return Json(zones.Select(z => new { id = z.Id, nom = z.Nom, prix = z.PrixLivraison }));
        }

        [HttpGet]
public IActionResult Panier()
{
    // Vous devrez implémenter la logique pour récupérer le panier
    var panier = new Panier(); // À remplacer par votre logique
    ViewBag.IsAuthenticated = _sessionService.IsAuthenticated();
    return View(panier);
}

[HttpGet]
public IActionResult Valider()
{
    var model = new CommandeValidationModel
    {
        Total = 0 // À remplacer par le total réel
    };
    ViewBag.IsAuthenticated = _sessionService.IsAuthenticated();
    return View(model);
}

[HttpPost]
public IActionResult Valider(CommandeValidationModel model)
{
    if (!ModelState.IsValid)
    {
        return View(model);
    }
    
    // Traitez la validation de commande ici
    
    return RedirectToAction("Confirmation");
}
    }
}