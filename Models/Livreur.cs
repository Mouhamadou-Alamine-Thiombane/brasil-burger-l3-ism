using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BrasilBurger.Models
{
    [Table("livreurs")]
    public class Livreur
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }
        
        [Required]
        [Column("nom")]
        [StringLength(100)]
        public required string Nom { get; set; }
        
        [Required]
        [Column("prenom")]
        [StringLength(100)]
        public required string Prenom { get; set; }
        
        [Required]
        [Column("telephone")]
        [StringLength(20)]
        public required string Telephone { get; set; }
        
        [Column("vehicule")]
        [StringLength(50)]
        public string? Vehicule { get; set; }
        
        [Column("disponible")]
        public bool Disponible { get; set; } = true;
        
        [Column("zone_id")]
        [ForeignKey("Zone")]
        public int? ZoneId { get; set; }
        public Zone? Zone { get; set; }
        
        [Column("archived")]
        public bool Archived { get; set; } = false;
        
        [Column("created_at")]
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
        
        [Column("updated_at")]
        public DateTime UpdatedAt { get; set; } = DateTime.UtcNow;
        
        public ICollection<Commande> Commandes { get; set; } = new List<Commande>();
    }
}   