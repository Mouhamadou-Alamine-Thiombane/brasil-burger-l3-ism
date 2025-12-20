using System;
using Microsoft.EntityFrameworkCore.Migrations;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;

#nullable disable

namespace BrasilBurger.Migrations
{
    /// <inheritdoc />
    public partial class InitialCreate : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            // migrationBuilder.CreateTable(
            //     name: "burgers",
            //     columns: table => new
            //     {
            //         id = table.Column<int>(type: "integer", nullable: false)
            //             .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
            //         nom = table.Column<string>(type: "character varying(100)", maxLength: 100, nullable: false),
            //         prix = table.Column<decimal>(type: "numeric", nullable: false),
            //         description = table.Column<string>(type: "text", nullable: true),
            //         image = table.Column<string>(type: "character varying(255)", maxLength: 255, nullable: true),
            //         archived = table.Column<bool>(type: "boolean", nullable: false),
            //         created_at = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
            //         updated_at = table.Column<DateTime>(type: "timestamp with time zone", nullable: false)
            //     },
            //     constraints: table =>
            //     {
            //         table.PrimaryKey("PK_burgers", x => x.id);
            //     });

            // migrationBuilder.CreateTable(
            //     name: "clients",
            //     columns: table => new
            //     {
            //         id = table.Column<int>(type: "integer", nullable: false)
            //             .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
            //         nom = table.Column<string>(type: "character varying(100)", maxLength: 100, nullable: false),
            //         prenom = table.Column<string>(type: "character varying(100)", maxLength: 100, nullable: false),
            //         telephone = table.Column<string>(type: "character varying(20)", maxLength: 20, nullable: false),
            //         email = table.Column<string>(type: "character varying(100)", maxLength: 100, nullable: false),
            //         mot_de_passe = table.Column<string>(type: "character varying(100)", maxLength: 100, nullable: false),
            //         adresse = table.Column<string>(type: "text", nullable: true),
            //         archived = table.Column<bool>(type: "boolean", nullable: false),
            //         created_at = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
            //         updated_at = table.Column<DateTime>(type: "timestamp with time zone", nullable: false)
            //     },
            //     constraints: table =>
            //     {
            //         table.PrimaryKey("PK_clients", x => x.id);
            //     });

            // migrationBuilder.CreateTable(
            //     name: "complements",
            //     columns: table => new
            //     {
            //         id = table.Column<int>(type: "integer", nullable: false)
            //             .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
            //         nom = table.Column<string>(type: "character varying(100)", maxLength: 100, nullable: false),
            //         prix = table.Column<decimal>(type: "numeric", nullable: false),
            //         type = table.Column<string>(type: "character varying(20)", maxLength: 20, nullable: false),
            //         image = table.Column<string>(type: "character varying(255)", maxLength: 255, nullable: true),
            //         archived = table.Column<bool>(type: "boolean", nullable: false),
            //         created_at = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
            //         updated_at = table.Column<DateTime>(type: "timestamp with time zone", nullable: false)
            //     },
            //     constraints: table =>
            //     {
            //         table.PrimaryKey("PK_complements", x => x.id);
            //     });

            // migrationBuilder.CreateTable(
            //     name: "zones",
            //     columns: table => new
            //     {
            //         id = table.Column<int>(type: "integer", nullable: false)
            //             .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
            //         nom = table.Column<string>(type: "character varying(100)", maxLength: 100, nullable: false),
            //         prix_livraison = table.Column<decimal>(type: "numeric", nullable: false),
            //         quartiers = table.Column<string[]>(type: "text[]", nullable: true),
            //         archived = table.Column<bool>(type: "boolean", nullable: false),
            //         created_at = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
            //         updated_at = table.Column<DateTime>(type: "timestamp with time zone", nullable: false)
            //     },
            //     constraints: table =>
            //     {
            //         table.PrimaryKey("PK_zones", x => x.id);
            //     });

            // migrationBuilder.CreateTable(
            //     name: "menus",
            //     columns: table => new
            //     {
            //         id = table.Column<int>(type: "integer", nullable: false)
            //             .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
            //         nom = table.Column<string>(type: "character varying(100)", maxLength: 100, nullable: false),
            //         burger_id = table.Column<int>(type: "integer", nullable: false),
            //         frite_id = table.Column<int>(type: "integer", nullable: true),
            //         boisson_id = table.Column<int>(type: "integer", nullable: true),
            //         image = table.Column<string>(type: "character varying(255)", maxLength: 255, nullable: true),
            //         prix = table.Column<decimal>(type: "numeric", nullable: false),
            //         archived = table.Column<bool>(type: "boolean", nullable: false),
            //         created_at = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
            //         updated_at = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
            //         ComplementId = table.Column<int>(type: "integer", nullable: true),
            //         ComplementId1 = table.Column<int>(type: "integer", nullable: true)
            //     },
            //     constraints: table =>
            //     {
            //         table.PrimaryKey("PK_menus", x => x.id);
            //         table.ForeignKey(
            //             name: "FK_menus_burgers_burger_id",
            //             column: x => x.burger_id,
            //             principalTable: "burgers",
            //             principalColumn: "id",
            //             onDelete: ReferentialAction.Restrict);
            //         table.ForeignKey(
            //             name: "FK_menus_complements_ComplementId",
            //             column: x => x.ComplementId,
            //             principalTable: "complements",
            //             principalColumn: "id");
            //         table.ForeignKey(
            //             name: "FK_menus_complements_ComplementId1",
            //             column: x => x.ComplementId1,
            //             principalTable: "complements",
            //             principalColumn: "id");
            //         table.ForeignKey(
            //             name: "FK_menus_complements_boisson_id",
            //             column: x => x.boisson_id,
            //             principalTable: "complements",
            //             principalColumn: "id",
            //             onDelete: ReferentialAction.Restrict);
            //         table.ForeignKey(
            //             name: "FK_menus_complements_frite_id",
            //             column: x => x.frite_id,
            //             principalTable: "complements",
            //             principalColumn: "id",
            //             onDelete: ReferentialAction.Restrict);
            //     });

            // migrationBuilder.CreateTable(
            //     name: "livreurs",
            //     columns: table => new
            //     {
            //         id = table.Column<int>(type: "integer", nullable: false)
            //             .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
            //         nom = table.Column<string>(type: "character varying(100)", maxLength: 100, nullable: false),
            //         prenom = table.Column<string>(type: "character varying(100)", maxLength: 100, nullable: false),
            //         telephone = table.Column<string>(type: "character varying(20)", maxLength: 20, nullable: false),
            //         vehicule = table.Column<string>(type: "character varying(50)", maxLength: 50, nullable: true),
            //         disponible = table.Column<bool>(type: "boolean", nullable: false),
            //         zone_id = table.Column<int>(type: "integer", nullable: true),
            //         archived = table.Column<bool>(type: "boolean", nullable: false),
            //         created_at = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
            //         updated_at = table.Column<DateTime>(type: "timestamp with time zone", nullable: false)
            //     },
            //     constraints: table =>
            //     {
            //         table.PrimaryKey("PK_livreurs", x => x.id);
            //         table.ForeignKey(
            //             name: "FK_livreurs_zones_zone_id",
            //             column: x => x.zone_id,
            //             principalTable: "zones",
            //             principalColumn: "id");
            //     });

            // migrationBuilder.CreateTable(
            //     name: "commandes",
            //     columns: table => new
            //     {
            //         id = table.Column<int>(type: "integer", nullable: false)
            //             .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
            //         client_id = table.Column<int>(type: "integer", nullable: false),
            //         etat = table.Column<string>(type: "text", nullable: false),
            //         type_livraison = table.Column<string>(type: "character varying(20)", maxLength: 20, nullable: false),
            //         date_commande = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
            //         date_livraison = table.Column<DateTime>(type: "timestamp with time zone", nullable: true),
            //         zone_id = table.Column<int>(type: "integer", nullable: true),
            //         livreur_id = table.Column<int>(type: "integer", nullable: true),
            //         adresse_livraison = table.Column<string>(type: "text", nullable: true),
            //         total = table.Column<decimal>(type: "numeric", nullable: false),
            //         payee = table.Column<bool>(type: "boolean", nullable: false),
            //         archived = table.Column<bool>(type: "boolean", nullable: false),
            //         created_at = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
            //         updated_at = table.Column<DateTime>(type: "timestamp with time zone", nullable: false)
            //     },
            //     constraints: table =>
            //     {
            //         table.PrimaryKey("PK_commandes", x => x.id);
            //         table.ForeignKey(
            //             name: "FK_commandes_clients_client_id",
            //             column: x => x.client_id,
            //             principalTable: "clients",
            //             principalColumn: "id",
            //             onDelete: ReferentialAction.Cascade);
            //         table.ForeignKey(
            //             name: "FK_commandes_livreurs_livreur_id",
            //             column: x => x.livreur_id,
            //             principalTable: "livreurs",
            //             principalColumn: "id");
            //         table.ForeignKey(
            //             name: "FK_commandes_zones_zone_id",
            //             column: x => x.zone_id,
            //             principalTable: "zones",
            //             principalColumn: "id");
            //     });

            // migrationBuilder.CreateTable(
            //     name: "commande_items",
            //     columns: table => new
            //     {
            //         id = table.Column<int>(type: "integer", nullable: false)
            //             .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
            //         commande_id = table.Column<int>(type: "integer", nullable: false),
            //         burger_id = table.Column<int>(type: "integer", nullable: true),
            //         menu_id = table.Column<int>(type: "integer", nullable: true),
            //         complement_id = table.Column<int>(type: "integer", nullable: true),
            //         quantite = table.Column<int>(type: "integer", nullable: false),
            //         prix_unitaire = table.Column<decimal>(type: "numeric", nullable: false),
            //         ComplementId1 = table.Column<int>(type: "integer", nullable: true)
            //     },
            //     constraints: table =>
            //     {
            //         table.PrimaryKey("PK_commande_items", x => x.id);
            //         table.CheckConstraint("CK_CommandeItem_Type", "(\r\n                        (burger_id IS NOT NULL AND menu_id IS NULL AND complement_id IS NULL)\r\n                     OR (burger_id IS NULL AND menu_id IS NOT NULL AND complement_id IS NULL)\r\n                     OR (burger_id IS NULL AND menu_id IS NULL AND complement_id IS NOT NULL)\r\n                    )");
            //         table.ForeignKey(
            //             name: "FK_commande_items_burgers_burger_id",
            //             column: x => x.burger_id,
            //             principalTable: "burgers",
            //             principalColumn: "id",
            //             onDelete: ReferentialAction.Restrict);
            //         table.ForeignKey(
            //             name: "FK_commande_items_commandes_commande_id",
            //             column: x => x.commande_id,
            //             principalTable: "commandes",
            //             principalColumn: "id",
            //             onDelete: ReferentialAction.Cascade);
            //         table.ForeignKey(
            //             name: "FK_commande_items_complements_ComplementId1",
            //             column: x => x.ComplementId1,
            //             principalTable: "complements",
            //             principalColumn: "id");
            //         table.ForeignKey(
            //             name: "FK_commande_items_complements_complement_id",
            //             column: x => x.complement_id,
            //             principalTable: "complements",
            //             principalColumn: "id",
            //             onDelete: ReferentialAction.Restrict);
            //         table.ForeignKey(
            //             name: "FK_commande_items_menus_menu_id",
            //             column: x => x.menu_id,
            //             principalTable: "menus",
            //             principalColumn: "id",
            //             onDelete: ReferentialAction.Restrict);
            //     });

            // migrationBuilder.CreateTable(
            //     name: "paiements",
            //     columns: table => new
            //     {
            //         id = table.Column<int>(type: "integer", nullable: false)
            //             .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
            //         commande_id = table.Column<int>(type: "integer", nullable: false),
            //         montant = table.Column<decimal>(type: "numeric", nullable: false),
            //         methode = table.Column<string>(type: "character varying(20)", maxLength: 20, nullable: false),
            //         date_paiement = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
            //         reference = table.Column<string>(type: "character varying(100)", maxLength: 100, nullable: true),
            //         archived = table.Column<bool>(type: "boolean", nullable: false),
            //         created_at = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
            //         updated_at = table.Column<DateTime>(type: "timestamp with time zone", nullable: false)
            //     },
            //     constraints: table =>
            //     {
            //         table.PrimaryKey("PK_paiements", x => x.id);
            //         table.ForeignKey(
            //             name: "FK_paiements_commandes_commande_id",
            //             column: x => x.commande_id,
            //             principalTable: "commandes",
            //             principalColumn: "id",
            //             onDelete: ReferentialAction.Cascade);
            //     });

            // migrationBuilder.CreateIndex(
            //     name: "IX_commande_items_burger_id",
            //     table: "commande_items",
            //     column: "burger_id");

            // migrationBuilder.CreateIndex(
            //     name: "IX_commande_items_commande_id",
            //     table: "commande_items",
            //     column: "commande_id");

            // migrationBuilder.CreateIndex(
            //     name: "IX_commande_items_complement_id",
            //     table: "commande_items",
            //     column: "complement_id");

            // migrationBuilder.CreateIndex(
            //     name: "IX_commande_items_ComplementId1",
            //     table: "commande_items",
            //     column: "ComplementId1");

            // migrationBuilder.CreateIndex(
            //     name: "IX_commande_items_menu_id",
            //     table: "commande_items",
            //     column: "menu_id");

            // migrationBuilder.CreateIndex(
            //     name: "IX_commandes_client_id",
            //     table: "commandes",
            //     column: "client_id");

            // migrationBuilder.CreateIndex(
            //     name: "IX_commandes_livreur_id",
            //     table: "commandes",
            //     column: "livreur_id");

            // migrationBuilder.CreateIndex(
            //     name: "IX_commandes_zone_id",
            //     table: "commandes",
            //     column: "zone_id");

            // migrationBuilder.CreateIndex(
            //     name: "IX_livreurs_zone_id",
            //     table: "livreurs",
            //     column: "zone_id");

            // migrationBuilder.CreateIndex(
            //     name: "IX_menus_boisson_id",
            //     table: "menus",
            //     column: "boisson_id");

            // migrationBuilder.CreateIndex(
            //     name: "IX_menus_burger_id",
            //     table: "menus",
            //     column: "burger_id");

            // migrationBuilder.CreateIndex(
            //     name: "IX_menus_ComplementId",
            //     table: "menus",
            //     column: "ComplementId");

            // migrationBuilder.CreateIndex(
            //     name: "IX_menus_ComplementId1",
            //     table: "menus",
            //     column: "ComplementId1");

            // migrationBuilder.CreateIndex(
            //     name: "IX_menus_frite_id",
            //     table: "menus",
            //     column: "frite_id");

            // migrationBuilder.CreateIndex(
            //     name: "IX_paiements_commande_id",
            //     table: "paiements",
            //     column: "commande_id");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            // migrationBuilder.DropTable(
            //     name: "commande_items");

            // migrationBuilder.DropTable(
            //     name: "paiements");

            // migrationBuilder.DropTable(
            //     name: "menus");

            // migrationBuilder.DropTable(
            //     name: "commandes");

            // migrationBuilder.DropTable(
            //     name: "burgers");

            // migrationBuilder.DropTable(
            //     name: "complements");

            // migrationBuilder.DropTable(
            //     name: "clients");

            // migrationBuilder.DropTable(
            //     name: "livreurs");

            // migrationBuilder.DropTable(
            //     name: "zones");
        }
    }
}
