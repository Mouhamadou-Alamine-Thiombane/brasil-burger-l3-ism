using Microsoft.AspNetCore.Mvc;
using BrasilBurger.Services;
using BrasilBurger.ViewModels;

namespace BrasilBurger.Controllers
{
    public class AuthController : Controller
    {
        private readonly AuthService _authService;
        private readonly SessionService _sessionService;

        public AuthController(AuthService authService, SessionService sessionService)
        {
            _authService = authService;
            _sessionService = sessionService;
        }

        [HttpGet]
        public IActionResult Login(string? returnUrl = null) // Changé à string?
        {
            if (_sessionService.IsAuthenticated())
            {
                return RedirectToAction("Index", "Home");
            }

            ViewBag.ReturnUrl = returnUrl;
            return View();
        }

        [HttpPost]
        public async Task<IActionResult> Login(LoginViewModel model, string? returnUrl = null) // Changé à string?
        {
            if (!ModelState.IsValid)
            {
                return View(model);
            }

            var client = await _authService.LoginAsync(model);
            if (client == null)
            {
                ModelState.AddModelError(string.Empty, "Email ou mot de passe incorrect");
                return View(model);
            }

            _sessionService.SetClientSession(client);

            if (!string.IsNullOrEmpty(returnUrl) && Url.IsLocalUrl(returnUrl))
            {
                return Redirect(returnUrl);
            }

            return RedirectToAction("Index", "Home");
        }

        [HttpGet]
        public IActionResult Register()
        {
            if (_sessionService.IsAuthenticated())
            {
                return RedirectToAction("Index", "Home");
            }

            return View();
        }

        [HttpPost]
        public async Task<IActionResult> Register(RegisterViewModel model)
        {
            if (!ModelState.IsValid)
            {
                return View(model);
            }

            var result = await _authService.RegisterAsync(model);
            if (!result.Success)
            {
                ModelState.AddModelError(string.Empty, result.Message);
                return View(model);
            }

            TempData["SuccessMessage"] = "Inscription réussie ! Vous pouvez maintenant vous connecter.";
            return RedirectToAction("Login");
        }

        [HttpPost]
        public IActionResult Logout()
        {
            _authService.Logout();
            _sessionService.ClearSession();
            return RedirectToAction("Index", "Home");
        }
    }
}