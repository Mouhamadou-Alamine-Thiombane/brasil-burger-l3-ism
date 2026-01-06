using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using BrasilBurger.Models;

namespace BrasilBurger.ViewModels
{
    public class CommandeViewModel
    {
        public int Id { get; set; }
        public DateTime DateCommande { get; set; }

        //public EtatCommande Etat { get; set; }
        public string Etat { get; set; } = string.Empty;
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

    // public class CreateCommandeViewModel
    // {
    //     public int? BurgerId { get; set; }
    //     public int? MenuId { get; set; }
    //     public List<int> ComplementIds { get; set; } = new List<int>();
    //     public string TypeLivraison { get; set; } = string.Empty;
    //     public int? ZoneId { get; set; }
    //     public string AdresseLivraison { get; set; } = string.Empty;
    //     public int Quantite { get; set; } = 1;
    // }

    public class CreateCommandeViewModel : IValidatableObject
    {
        public int? BurgerId { get; set; }
        public int? MenuId { get; set; }
        public List<int> ComplementIds { get; set; } = new List<int>();
        
        [Required(ErrorMessage = "Le type de livraison est requis")]
        public string TypeLivraison { get; set; } = string.Empty;
        
        public int? ZoneId { get; set; }
        
        public string? AdresseLivraison { get; set; } // Changez en nullable
        
        [Required(ErrorMessage = "La quantité est requise")]
        [Range(1, 10, ErrorMessage = "La quantité doit être entre 1 et 10")]
        public int Quantite { get; set; } = 1;

        public IEnumerable<ValidationResult> Validate(ValidationContext validationContext)
        {
            // Validation personnalisée
            if (TypeLivraison == "LIVRAISON")
            {
                if (!ZoneId.HasValue)
                {
                    yield return new ValidationResult(
                        "La zone de livraison est requise pour la livraison",
                        new[] { nameof(ZoneId) });
                }

                if (string.IsNullOrWhiteSpace(AdresseLivraison))
                {
                    yield return new ValidationResult(
                        "L'adresse de livraison est requise pour la livraison",
                        new[] { nameof(AdresseLivraison) });
                }
            }
        }
    }
}