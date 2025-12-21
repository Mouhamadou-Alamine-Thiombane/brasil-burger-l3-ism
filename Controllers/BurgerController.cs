using Microsoft.AspNetCore.Mvc;
using BrasilBurger.Services;
using BrasilBurger.ViewModels;

namespace BrasilBurger.Controllers
{
    public class BurgerController : Controller
    {
        private readonly BurgerService _burgerService;
        private readonly SessionService _sessionService;

        public BurgerController(BurgerService burgerService, SessionService sessionService)
        {
            _burgerService = burgerService;
            _sessionService = sessionService;
        }

        [HttpGet]
        public async Task<IActionResult> Detail(int id)
        {
            var burger = await _burgerService.GetBurgerDetailAsync(id);
            if (burger == null)
            {
                return NotFound();
            }

            ViewBag.IsAuthenticated = _sessionService.IsAuthenticated();
            ViewBag.ClientNom = _sessionService.GetClientNom();
            ViewBag.ClientPrenom = _sessionService.GetClientPrenom();
            return View(burger);
        }

        [HttpGet]
        public async Task<IActionResult> MenuDetail(int id)
        {
            var menu = await _burgerService.GetMenuDetailAsync(id);
            if (menu == null)
            {
                return NotFound();
            }

            ViewBag.IsAuthenticated = _sessionService.IsAuthenticated();
            return View(menu);
        }

        [HttpGet]
        public async Task<IActionResult> CommanderBurger(int id)
        {
            if (!_sessionService.IsAuthenticated())
            {
                return RedirectToAction("Login", "Auth", new { returnUrl = Url.Action("CommanderBurger", new { id }) });
            }

            var burger = await _burgerService.GetBurgerDetailAsync(id);
            if (burger == null)
            {
                return NotFound();
            }

            var complements = await _burgerService.GetComplementsAsync();
            ViewBag.Complements = complements;

            var model = new CreateCommandeViewModel
            {
                BurgerId = id
            };

            ViewBag.IsAuthenticated = true;
            ViewBag.ClientNom = _sessionService.GetClientNom();
            return View("Commander", model);
        }

        [HttpGet]
        public async Task<IActionResult> CommanderMenu(int id)
        {
            if (!_sessionService.IsAuthenticated())
            {
                return RedirectToAction("Login", "Auth", new { returnUrl = Url.Action("CommanderMenu", new { id }) });
            }

            var menu = await _burgerService.GetMenuDetailAsync(id);
            if (menu == null)
            {
                return NotFound();
            }

            var model = new CreateCommandeViewModel
            {
                MenuId = id
            };

            ViewBag.IsAuthenticated = true;
            ViewBag.ClientNom = _sessionService.GetClientNom();
            return View("Commander", model);
        }
    }
}