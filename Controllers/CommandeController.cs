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
    Console.WriteLine($"=== DÉBUT CRÉATION COMMANDE ===");
    
    // LOG TOUTES LES DONNÉES RECUES
    Console.WriteLine($"BurgerId: {model.BurgerId}");
    Console.WriteLine($"TypeLivraison: {model.TypeLivraison}");
    Console.WriteLine($"Quantite: {model.Quantite}");
    Console.WriteLine($"ComplementIds: {(model.ComplementIds != null ? string.Join(",", model.ComplementIds) : "null")}");
    Console.WriteLine($"ZoneId: {model.ZoneId}");
    Console.WriteLine($"AdresseLivraison: {model.AdresseLivraison}");
    
    if (!_sessionService.IsAuthenticated())
    {
        return RedirectToAction("Login", "Auth");
    }

    // ✅ FIX: Valider manuellement au lieu d'utiliser ModelState.IsValid
    var validationErrors = new List<string>();
    
    if (string.IsNullOrEmpty(model.TypeLivraison))
    {
        validationErrors.Add("Le type de livraison est requis");
    }
    
    if (model.Quantite < 1)
    {
        validationErrors.Add("La quantité doit être au moins 1");
    }
    
    if (model.TypeLivraison == "LIVRAISON")
    {
        if (!model.ZoneId.HasValue)
        {
            validationErrors.Add("La zone de livraison est requise pour la livraison");
        }
        
        if (string.IsNullOrWhiteSpace(model.AdresseLivraison))
        {
            validationErrors.Add("L'adresse de livraison est requise pour la livraison");
        }
    }
    
    // Si pas de burger ni menu
    if (!model.BurgerId.HasValue && !model.MenuId.HasValue)
    {
        validationErrors.Add("Aucun produit sélectionné");
    }
    
    // ✅ FIX: Nettoyer l'adresse si ce n'est pas une livraison
    if (model.TypeLivraison != "LIVRAISON")
    {
        model.AdresseLivraison = null;
        model.ZoneId = null;
    }
    
    if (validationErrors.Any())
    {
        Console.WriteLine($"❌ Erreurs de validation: {string.Join(", ", validationErrors)}");
        
        // Retourner à la page avec les erreurs
        if (model.BurgerId.HasValue)
        {
            var burger = await _burgerService.GetBurgerDetailAsync(model.BurgerId.Value);
            if (burger != null)
            {
                ViewBag.Complements = await _burgerService.GetComplementsAsync();
                ViewBag.IsAuthenticated = true;
                ViewBag.ErrorMessage = string.Join("<br>", validationErrors);
                return View("~/Views/Burger/Commander.cshtml", model);
            }
        }
        
        TempData["ErrorMessage"] = string.Join(". ", validationErrors);
        return RedirectToAction("Index", "Home");
    }

    var clientId = _sessionService.GetClientId();
    if (!clientId.HasValue)
    {
        return RedirectToAction("Login", "Auth");
    }
    
    try
    {
        Console.WriteLine($"✅ Tentative de création de commande pour client {clientId.Value}");
        var commande = await _commandeService.CreateCommandeAsync(clientId.Value, model);
        Console.WriteLine($"✅ Commande créée avec ID: {commande.Id}");
        
        // Redirection vers la page de paiement
        return RedirectToAction("Paiement", "Paiement", new { commandeId = commande.Id });
    }
    catch (Exception ex)
    {
        Console.WriteLine($"❌ Erreur création commande: {ex.Message}");
        Console.WriteLine($"❌ StackTrace: {ex.StackTrace}");
        
        // Retourner à la page avec l'erreur
        ViewBag.ErrorMessage = $"Erreur: {ex.Message}";
        ViewBag.IsAuthenticated = true;
        
        if (model.BurgerId.HasValue)
        {
            ViewBag.Complements = await _burgerService.GetComplementsAsync();
        }
        
        return View("~/Views/Burger/Commander.cshtml", model);
    }
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