using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BrasilBurger.Models
{
    // [Table("burgers")]
    [Table("burgers", Schema = "public")]
    public class Burger
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }

        [Required(ErrorMessage = "Le nom est requis")]
        [Column("nom")]
        [StringLength(100)]
        public required string Nom { get; set; }

        [Required(ErrorMessage = "Le prix est requis")]
        [Column("prix")]
        [Range(0.01, double.MaxValue, ErrorMessage = "Le prix doit être supérieur à 0")]
        public decimal Prix { get; set; }

        [Column("description")]
        public string? Description { get; set; }

        [Column("image")]
        [StringLength(255)]
        public string? Image { get; set; }

        [Column("archived")]
        public bool Archived { get; set; } = false;

        [Column("created_at")]
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;

        [Column("updated_at")]
        public DateTime UpdatedAt { get; set; } = DateTime.UtcNow;

        
        public ICollection<CommandeItem> CommandeItems { get; set; } = new List<CommandeItem>();

        
        public ICollection<Menu> Menus { get; set; } = new List<Menu>();
    }
}
