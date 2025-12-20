using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BrasilBurger.Models
{
    [Table("menus")]
    public class Menu
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }
        
        [Required(ErrorMessage = "Le nom est requis")]
        [Column("nom")]
        [StringLength(100)]
        public required string Nom { get; set; }
        
        [Required]
        [Column("burger_id")]
        [ForeignKey("Burger")]
        public int BurgerId { get; set; }
        public Burger? Burger { get; set; }
        
        // ✅ CORRECTION : Utiliser des noms de propriétés différents
        [Column("frite_id")]
        [ForeignKey("Frite")]
        public int? FriteId { get; set; }
        public Complement? Frite { get; set; }
        
        [Column("boisson_id")]
        [ForeignKey("Boisson")]
        public int? BoissonId { get; set; }
        public Complement? Boisson { get; set; }
        
        [Column("image")]
        [StringLength(255)]
        public string? Image { get; set; }
        
        [Required]
        [Column("prix")]
        [Range(0.01, double.MaxValue, ErrorMessage = "Le prix doit être supérieur à 0")]
        public decimal Prix { get; set; }
        
        [Column("archived")]
        public bool Archived { get; set; } = false;
        
        [Column("created_at")]
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
        
        [Column("updated_at")]
        public DateTime UpdatedAt { get; set; } = DateTime.UtcNow;

        public ICollection<CommandeItem> CommandeItems { get; set; } = new List<CommandeItem>();
    }
}