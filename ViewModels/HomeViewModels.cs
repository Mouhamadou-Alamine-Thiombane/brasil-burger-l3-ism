namespace BrasilBurger.ViewModels
{
    public class HomeViewModel
    {
        public List<BurgerViewModel> Burgers { get; set; } = new List<BurgerViewModel>();
        public List<MenuViewModel> Menus { get; set; } = new List<MenuViewModel>();
        public List<BurgerViewModel> BurgersPopulaires { get; set; } = new List<BurgerViewModel>();
        public List<MenuViewModel> MenusPopulaires { get; set; } = new List<MenuViewModel>();
    }

    public class CatalogueViewModel
    {
        public List<BurgerViewModel> Burgers { get; set; } = new List<BurgerViewModel>();
        public List<MenuViewModel> Menus { get; set; } = new List<MenuViewModel>();
        public string Type { get; set; } = "burgers"; // Valeur par défaut "burgers" ou "menus"
    }
}