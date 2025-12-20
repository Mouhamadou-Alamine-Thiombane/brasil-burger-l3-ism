using System.ComponentModel.DataAnnotations;

namespace BrasilBurger.ViewModels
{
    public class CompteViewModel
    {
        public int Id { get; set; }
       
        [Required(ErrorMessage = "Le nom est requis")]
        public string Nom { get; set; } = string.Empty;
       
        [Required(ErrorMessage = "Le prénom est requis")]
        public string Prenom { get; set; } = string.Empty;
       
        [Required(ErrorMessage = "Le téléphone est requis")]
        [Phone(ErrorMessage = "Format de téléphone invalide")]
        public string Telephone { get; set; } = string.Empty;
       
        [Required(ErrorMessage = "L'email est requis")]
        [EmailAddress(ErrorMessage = "Format d'email invalide")]
        public string Email { get; set; } = string.Empty;
       
        public string? Adresse { get; set; }
       
        public List<CommandeViewModel> Commandes { get; set; } = new List<CommandeViewModel>();
        
        // Ajoutez ces propriétés pour compatibilité
        public int NombreCommandesTotal => Commandes.Count;
        public decimal MontantTotalDepense => Commandes.Sum(c => c.Total);
        
        // Propriétés pour le client (renommées)
        public string ClientNom => Nom;
        public string ClientPrenom => Prenom;
        public string ClientEmail => Email;
        public string ClientTelephone => Telephone;
        
        public List<CommandeViewModel> CommandesRecentes => Commandes.Take(5).ToList();
    }

    public class UpdateCompteViewModel
    {
        [Required(ErrorMessage = "Le nom est requis")]
        public string Nom { get; set; } = string.Empty;

        [Required(ErrorMessage = "Le prénom est requis")]
        public string Prenom { get; set; } = string.Empty;

        [Required(ErrorMessage = "Le téléphone est requis")]
        [Phone(ErrorMessage = "Format de téléphone invalide")]
        public string Telephone { get; set; } = string.Empty;

        [Required(ErrorMessage = "L'email est requis")]
        [EmailAddress(ErrorMessage = "Format d'email invalide")]
        public string Email { get; set; } = string.Empty;

        public string? Adresse { get; set; }

        [DataType(DataType.Password)]
        public string? CurrentPassword { get; set; }

        [DataType(DataType.Password)]
        [MinLength(6, ErrorMessage = "Le mot de passe doit avoir au moins 6 caractères")]
        public string? NewPassword { get; set; }

        [DataType(DataType.Password)]
        [Compare("NewPassword", ErrorMessage = "Les mots de passe ne correspondent pas")]
        public string? ConfirmNewPassword { get; set; }
    }
}