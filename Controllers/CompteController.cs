using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using BrasilBurger.Services;
using BrasilBurger.ViewModels;
using BrasilBurger.Data;

namespace BrasilBurger.Controllers
{
    public class CompteController : Controller
    {
        private readonly SessionService _sessionService;
        private readonly AuthService _authService;
        private readonly CommandeService _commandeService;
        private readonly ApplicationDbContext _context;

        public CompteController(
            SessionService sessionService,
            AuthService authService,
            CommandeService commandeService,
            ApplicationDbContext context)
        {
            _sessionService = sessionService;
            _authService = authService;
            _commandeService = commandeService;
            _context = context;
        }

        [HttpGet]
        public async Task<IActionResult> Index()  // ← AJOUTEZ 'async' ici
        {
            if (!_sessionService.IsAuthenticated())
            {
                return RedirectToAction("Login", "Auth");
            }

            var client = await _authService.GetCurrentClientAsync();  // ← Ça fonctionnera maintenant
            if (client == null)
            {
                _sessionService.ClearSession();
                return RedirectToAction("Login", "Auth");
            }

            var commandes = await _commandeService.GetClientCommandesAsync(client.Id);

            var model = new CompteViewModel
            {
                Id = client.Id,
                Nom = client.Nom,
                Prenom = client.Prenom,
                Telephone = client.Telephone,
                Email = client.Email,
                Adresse = client.Adresse,
                Commandes = commandes
            };

            ViewBag.IsAuthenticated = true;
            ViewBag.ClientNom = client.Nom;
            ViewBag.ClientPrenom = client.Prenom;
            return View(model);
        }

        [HttpGet]
        public async Task<IActionResult> Edit()  // ← AJOUTEZ 'async' ici
        {
            if (!_sessionService.IsAuthenticated())
            {
                return RedirectToAction("Login", "Auth");
            }

            var client = await _authService.GetCurrentClientAsync();  // ← Ça fonctionnera maintenant
            if (client == null)
            {
                _sessionService.ClearSession();
                return RedirectToAction("Login", "Auth");
            }

            var model = new UpdateCompteViewModel
            {
                Nom = client.Nom,
                Prenom = client.Prenom,
                Telephone = client.Telephone,
                Email = client.Email,
                Adresse = client.Adresse
            };

            ViewBag.IsAuthenticated = true;
            ViewBag.ClientNom = client.Nom;
            return View(model);
        }

        [HttpPost]
        public async Task<IActionResult> Edit(UpdateCompteViewModel model)  // ← Déjà 'async'
        {
            if (!_sessionService.IsAuthenticated())
            {
                return RedirectToAction("Login", "Auth");
            }

            if (!ModelState.IsValid)
            {
                ViewBag.IsAuthenticated = true;
                return View(model);
            }

            var client = await _authService.GetCurrentClientAsync();
            if (client == null)
            {
                _sessionService.ClearSession();
                return RedirectToAction("Login", "Auth");
            }

            // Check if email is already used by another client
            if (client.Email != model.Email)
            {
                var existingClient = await _context.Clients
                    .FirstOrDefaultAsync(c => c.Email == model.Email && c.Id != client.Id);
                if (existingClient != null)
                {
                    ModelState.AddModelError("Email", "Cet email est déjà utilisé");
                    ViewBag.IsAuthenticated = true;
                    return View(model);
                }
            }

            // Check if phone is already used by another client
            if (client.Telephone != model.Telephone)
            {
                var existingClient = await _context.Clients
                    .FirstOrDefaultAsync(c => c.Telephone == model.Telephone && c.Id != client.Id);
                if (existingClient != null)
                {
                    ModelState.AddModelError("Telephone", "Ce numéro de téléphone est déjà utilisé");
                    ViewBag.IsAuthenticated = true;
                    return View(model);
                }
            }

            // Update password if provided
            if (!string.IsNullOrEmpty(model.NewPassword))
            {
                if (string.IsNullOrEmpty(model.CurrentPassword))
                {
                    ModelState.AddModelError("CurrentPassword", "Le mot de passe actuel est requis");
                    ViewBag.IsAuthenticated = true;
                    return View(model);
                }

                if (client.MotDePasse != model.CurrentPassword)
                {
                    ModelState.AddModelError("CurrentPassword", "Le mot de passe actuel est incorrect");
                    ViewBag.IsAuthenticated = true;
                    return View(model);
                }

                client.MotDePasse = model.NewPassword;
            }

            // Update client information
            client.Nom = model.Nom;
            client.Prenom = model.Prenom;
            client.Telephone = model.Telephone;
            client.Email = model.Email;
            client.Adresse = model.Adresse;
            client.UpdatedAt = DateTime.UtcNow;

            _context.Clients.Update(client);
            await _context.SaveChangesAsync();

            // Update session
            _sessionService.SetClientSession(client);

            TempData["SuccessMessage"] = "Profil mis à jour avec succès";
            return RedirectToAction("Index");
        }

        // AJOUTEZ cette nouvelle méthode pour Profil
        [HttpGet]
        public async Task<IActionResult> Profil()  // ← AJOUTEZ 'async' ici
        {
            if (!_sessionService.IsAuthenticated())
            {
                return RedirectToAction("Login", "Auth");
            }
            
            var client = await _authService.GetCurrentClientAsync();  // ← Ça fonctionnera maintenant
            if (client == null)
            {
                return RedirectToAction("Login", "Auth");
            }
            
            var model = new CompteModifierProfilModel
            {
                Nom = client.Nom,
                Prenom = client.Prenom,
                Email = client.Email,
                Telephone = client.Telephone,
                Adresse = client.Adresse
            };
            
            ViewBag.IsAuthenticated = true;
            return View(model);
        }

        [HttpPost]
        public async Task<IActionResult> Profil(CompteModifierProfilModel model)  // ← AJOUTEZ 'async' ici
        {
            if (!_sessionService.IsAuthenticated())
            {
                return RedirectToAction("Login", "Auth");
            }
            
            if (!ModelState.IsValid)
            {
                ViewBag.IsAuthenticated = true;
                return View(model);
            }
            
            var client = await _authService.GetCurrentClientAsync();
            if (client == null)
            {
                _sessionService.ClearSession();
                return RedirectToAction("Login", "Auth");
            }

            // Vérifiez si l'email est déjà utilisé par un autre client
            if (client.Email != model.Email)
            {
                var existingClient = await _context.Clients
                    .FirstOrDefaultAsync(c => c.Email == model.Email && c.Id != client.Id);
                if (existingClient != null)
                {
                    ModelState.AddModelError("Email", "Cet email est déjà utilisé");
                    ViewBag.IsAuthenticated = true;
                    return View(model);
                }
            }

            // Vérifiez si le téléphone est déjà utilisé par un autre client
            if (client.Telephone != model.Telephone)
            {
                var existingClient = await _context.Clients
                    .FirstOrDefaultAsync(c => c.Telephone == model.Telephone && c.Id != client.Id);
                if (existingClient != null)
                {
                    ModelState.AddModelError("Telephone", "Ce numéro de téléphone est déjà utilisé");
                    ViewBag.IsAuthenticated = true;
                    return View(model);
                }
            }

            // Mettre à jour le mot de passe si fourni
            if (!string.IsNullOrEmpty(model.NewPassword))
            {
                if (string.IsNullOrEmpty(model.CurrentPassword))
                {
                    ModelState.AddModelError("CurrentPassword", "Le mot de passe actuel est requis");
                    ViewBag.IsAuthenticated = true;
                    return View(model);
                }

                // Note: Dans une vraie application, vous devriez hasher le mot de passe
                if (client.MotDePasse != model.CurrentPassword)
                {
                    ModelState.AddModelError("CurrentPassword", "Le mot de passe actuel est incorrect");
                    ViewBag.IsAuthenticated = true;
                    return View(model);
                }

                client.MotDePasse = model.NewPassword;
            }

            // Mettre à jour les informations du client
            client.Nom = model.Nom;
            client.Prenom = model.Prenom;
            client.Telephone = model.Telephone;
            client.Email = model.Email;
            client.Adresse = model.Adresse;
            client.UpdatedAt = DateTime.UtcNow;

            _context.Clients.Update(client);
            await _context.SaveChangesAsync();

            // Mettre à jour la session
            _sessionService.SetClientSession(client);

            TempData["SuccessMessage"] = "Profil mis à jour avec succès";
            return RedirectToAction("Profil");
        }
    }
}