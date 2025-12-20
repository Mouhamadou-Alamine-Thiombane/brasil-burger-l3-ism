using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BrasilBurger.Models
{
    [Table("paiements")]
    public class Paiement
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }
        
        [Required]
        [Column("commande_id")]
        [ForeignKey("Commande")]
        public int CommandeId { get; set; }
        public Commande? Commande { get; set; }
        
        [Required]
        [Column("montant")]
        [Range(0.01, double.MaxValue, ErrorMessage = "Le montant doit être supérieur à 0")]
        public decimal Montant { get; set; }
        
        [Required]
        [Column("methode")]
        [StringLength(20)]
        public required string Methode { get; set; } // "WAVE", "OM", "CARTE", "ESPECES"
        
        [Column("date_paiement")]
        public DateTime DatePaiement { get; set; } = DateTime.UtcNow;

        [Column("reference")]
        [StringLength(100)]
        public string? Reference { get; set; }
        
        [Column("archived")]
        public bool Archived { get; set; } = false;
        
        [Column("created_at")]
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
        
        [Column("updated_at")]
        public DateTime UpdatedAt { get; set; } = DateTime.UtcNow;
    }
}
