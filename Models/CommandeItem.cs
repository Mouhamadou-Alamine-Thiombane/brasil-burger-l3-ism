using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BrasilBurger.Models
{
    [Table("commande_items")]
    public class CommandeItem
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }
        
        [Required]
        [Column("commande_id")]
        [ForeignKey("Commande")]
        public int CommandeId { get; set; }
        public Commande? Commande { get; set; }
        
        [Column("burger_id")]
        [ForeignKey("Burger")]
        public int? BurgerId { get; set; }
        public Burger? Burger { get; set; }
        
        [Column("menu_id")]
        [ForeignKey("Menu")]
        public int? MenuId { get; set; }
        public Menu? Menu { get; set; }
        
        // ✅ CORRECTION : Bien spécifier la propriété de navigation
        [Column("complement_id")]
        [ForeignKey(nameof(Complement))]
        public int? ComplementId { get; set; }
        public Complement? Complement { get; set; }
        
        [Required]
        [Column("quantite")]
        [Range(1, int.MaxValue, ErrorMessage = "La quantité doit être supérieure à 0")]
        public int Quantite { get; set; }
        
        [Required]
        [Column("prix_unitaire")]
        [Range(0, double.MaxValue, ErrorMessage = "Le prix unitaire ne peut pas être négatif")]
        public decimal PrixUnitaire { get; set; }
    }
}