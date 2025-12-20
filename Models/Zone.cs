using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BrasilBurger.Models
{
    [Table("zones")]
    public class Zone
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }
        
        [Required]
        [Column("nom")]
        [StringLength(100)]
        public required string Nom { get; set; }
        
        [Required]
        [Column("prix_livraison")]
        [Range(0, double.MaxValue, ErrorMessage = "Le prix de livraison ne peut pas être négatif")]
        public decimal PrixLivraison { get; set; }
        
        [Column("quartiers")]
        public string[]? Quartiers { get; set; }
        
        [Column("archived")]
        public bool Archived { get; set; } = false;
        
        [Column("created_at")]
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
        
        [Column("updated_at")]
        public DateTime UpdatedAt { get; set; } = DateTime.UtcNow;
        
        public ICollection<Commande> Commandes { get; set; } = new List<Commande>();
        public ICollection<Livreur> Livreurs { get; set; } = new List<Livreur>();
    }
}
