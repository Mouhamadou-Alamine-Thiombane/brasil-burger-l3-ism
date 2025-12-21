
using BrasilBurger.Models;

namespace BrasilBurger.ViewModels
{
    public class CommandeViewModel
    {
        public int Id { get; set; }
        public DateTime DateCommande { get; set; }

        public EtatCommande Etat { get; set; }
        //public string Etat { get; set; } = string.Empty;
        public string TypeLivraison { get; set; } = string.Empty;
        public decimal Total { get; set; }
        public bool Payee { get; set; }
        public string? AdresseLivraison { get; set; }
        public List<CommandeItemViewModel> Items { get; set; } = new List<CommandeItemViewModel>();

        public decimal MontantTotal => Total;

        public string Statut => Etat.ToString();
        //public string Statut => Etat;

    }

    public class CommandeItemViewModel
    {
        public string Type { get; set; } = string.Empty; // "Burger", "Menu" ou "Complement"
        public string Nom { get; set; } = string.Empty;
        public int Quantite { get; set; }
        public decimal PrixUnitaire { get; set; }
        public decimal Total => Quantite * PrixUnitaire;
    }

    public class CreateCommandeViewModel
    {
        public int? BurgerId { get; set; }
        public int? MenuId { get; set; }
        public List<int> ComplementIds { get; set; } = new List<int>();
        public string TypeLivraison { get; set; } = string.Empty;
        public int? ZoneId { get; set; }
        public string AdresseLivraison { get; set; } = string.Empty;
        public int Quantite { get; set; } = 1;
    }
}