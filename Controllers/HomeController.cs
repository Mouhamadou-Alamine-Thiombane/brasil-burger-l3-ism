using Microsoft.AspNetCore.Mvc;
using BrasilBurger.Services;
using BrasilBurger.ViewModels;

namespace BrasilBurger.Controllers
{
    public class HomeController : Controller
    {
        private readonly BurgerService _burgerService;
        private readonly SessionService _sessionService;

        public HomeController(BurgerService burgerService, SessionService sessionService)
        {
            _burgerService = burgerService;
            _sessionService = sessionService;
        }

        public async Task<IActionResult> Index()
        {
            var model = new HomeViewModel
            {
                Burgers = await _burgerService.GetAllBurgersAsync(),
                Menus = await _burgerService.GetAllMenusAsync(),
                BurgersPopulaires = await _burgerService.GetPopularBurgersAsync(),
                MenusPopulaires = await _burgerService.GetPopularMenusAsync()
            };

            ViewBag.IsAuthenticated = _sessionService.IsAuthenticated();
            ViewBag.ClientNom = _sessionService.GetClientNom();
            ViewBag.ClientPrenom = _sessionService.GetClientPrenom();

            return View(model);
        }

        public async Task<IActionResult> Catalogue(string type = "burgers")
        {
            var model = new CatalogueViewModel
            {
                Type = type
            };

            if (type == "burgers")
            {
                model.Burgers = await _burgerService.GetAllBurgersAsync();
            }
            else
            {
                model.Menus = await _burgerService.GetAllMenusAsync();
            }

            ViewBag.IsAuthenticated = _sessionService.IsAuthenticated();
            return View(model);
        }

        public IActionResult About()
        {
            ViewBag.IsAuthenticated = _sessionService.IsAuthenticated();
            return View();
        }

        public IActionResult Contact()
        {
            ViewBag.IsAuthenticated = _sessionService.IsAuthenticated();
            return View();
        }

        public IActionResult Privacy()
        {
            ViewBag.IsAuthenticated = _sessionService.IsAuthenticated();
            return View();
        }

        [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
        public IActionResult Error()
        {
            return View();
        }
    }
}