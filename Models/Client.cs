using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BrasilBurger.Models
{
    [Table("clients")]
    public class Client
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }
        
        [Required(ErrorMessage = "Le nom est requis")]
        [Column("nom")]
        [StringLength(100)]
        public required string Nom { get; set; }
        
        [Required(ErrorMessage = "Le prénom est requis")]
        [Column("prenom")]
        [StringLength(100)]
        public required string Prenom { get; set; }
        
        [Required(ErrorMessage = "Le téléphone est requis")]
        [Column("telephone")]
        [StringLength(20)]
        [Phone(ErrorMessage = "Format de téléphone invalide")]
        public required string Telephone { get; set; }
        
        [Required(ErrorMessage = "L'email est requis")]
        [Column("email")]
        [StringLength(100)]
        [EmailAddress(ErrorMessage = "Format d'email invalide")]
        public required string Email { get; set; }
        
        [Required(ErrorMessage = "Le mot de passe est requis")]
        [Column("mot_de_passe")]
        [StringLength(100)]
        [DataType(DataType.Password)]
        public required string MotDePasse { get; set; }
        
        [Column("adresse")]
        public string? Adresse { get; set; }
        
        [Column("archived")]
        public bool Archived { get; set; } = false;
        
        [Column("created_at")]
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
        
        [Column("updated_at")]
        public DateTime UpdatedAt { get; set; } = DateTime.UtcNow;
        
        public ICollection<Commande> Commandes { get; set; } = new List<Commande>();
    }
}