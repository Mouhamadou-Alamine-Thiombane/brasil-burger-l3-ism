namespace BrasilBurger.ViewModels
{
    public class BurgerDetailViewModel
    {
        public int Id { get; set; }
        public string Nom { get; set; } = string.Empty;
        public decimal Prix { get; set; }
        public string Description { get; set; } = string.Empty;
        public string Image { get; set; } = string.Empty;
        public List<ComplementViewModel> Complements { get; set; } = new List<ComplementViewModel>();
    }

    public class ComplementViewModel
    {
        public int Id { get; set; }
        public string Nom { get; set; } = string.Empty;
        public decimal Prix { get; set; }
        public string Type { get; set; } = string.Empty;
        public bool Selected { get; set; }
    }

    public class MenuViewModel
    {
        public int Id { get; set; }
        public string Nom { get; set; } = string.Empty;
        public BurgerViewModel Burger { get; set; } = new BurgerViewModel();
        public ComplementViewModel? Frite { get; set; }
        public ComplementViewModel? Boisson { get; set; }
        public decimal Prix { get; set; }
        public string Image { get; set; } = string.Empty;
        // Ajoutez ces propriétés pour compatibilité
        public string ImageUrl => Image;
        public decimal PrixTotal => Prix;
        public ComplementViewModel? Complement => Frite ?? Boisson;
    }

    public class BurgerViewModel
    {
        public int Id { get; set; }
        public string Nom { get; set; } = string.Empty;
        public decimal Prix { get; set; }
        public string Description { get; set; } = string.Empty;
        public string Image { get; set; } = string.Empty;
        // Ajoutez cette propriété pour compatibilité
        public string ImageUrl => Image;
    }
}