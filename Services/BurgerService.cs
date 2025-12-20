using Microsoft.EntityFrameworkCore;
using BrasilBurger.Data;
using BrasilBurger.Models;
using BrasilBurger.ViewModels;

namespace BrasilBurger.Services
{
    public class BurgerService
    {
        private readonly ApplicationDbContext _context;

        public BurgerService(ApplicationDbContext context)
        {
            _context = context;
        }

        // ============================
        // TOUS LES BURGERS
        // ============================
        public async Task<List<BurgerViewModel>> GetAllBurgersAsync()
        {
            return await _context.Burgers
                .Where(b => !b.Archived)
                .Select(b => new BurgerViewModel
                {
                    Id = b.Id,
                    Nom = b.Nom,
                    Prix = b.Prix,
                    Description = b.Description ?? string.Empty,
                    Image = b.Image ?? string.Empty
                })
                .ToListAsync();
        }

        // ============================
        // TOUS LES MENUS
        // ============================
        public async Task<List<MenuViewModel>> GetAllMenusAsync()
        {
            return await _context.Menus
                .Where(m => !m.Archived)
                .Include(m => m.Burger)
                .Include(m => m.Frite)
                .Include(m => m.Boisson)
                .Select(m => new MenuViewModel
                {
                    Id = m.Id,
                    Nom = m.Nom,
                    Burger = new BurgerViewModel
                    {
                        Id = m.Burger!.Id,
                        Nom = m.Burger.Nom,
                        Prix = m.Burger.Prix,
                        Description = m.Burger.Description ?? string.Empty,
                        Image = m.Burger.Image ?? string.Empty
                    },
                    Frite = m.Frite != null ? new ComplementViewModel
                    {
                        Id = m.Frite.Id,
                        Nom = m.Frite.Nom,
                        Prix = m.Frite.Prix,
                        Type = m.Frite.Type
                    } : null,
                    Boisson = m.Boisson != null ? new ComplementViewModel
                    {
                        Id = m.Boisson.Id,
                        Nom = m.Boisson.Nom,
                        Prix = m.Boisson.Prix,
                        Type = m.Boisson.Type
                    } : null,
                    Prix = m.Prix,
                    Image = m.Image ?? string.Empty
                })
                .ToListAsync();
        }

        // ============================
        // DÉTAIL BURGER
        // ============================
        public async Task<BurgerDetailViewModel?> GetBurgerDetailAsync(int id)
        {
            var burger = await _context.Burgers
                .FirstOrDefaultAsync(b => b.Id == id && !b.Archived);

            if (burger == null)
                return null;

            var complements = await _context.Complements
                .Where(c => !c.Archived)
                .Select(c => new ComplementViewModel
                {
                    Id = c.Id,
                    Nom = c.Nom,
                    Prix = c.Prix,
                    Type = c.Type
                })
                .ToListAsync();

            return new BurgerDetailViewModel
            {
                Id = burger.Id,
                Nom = burger.Nom,
                Prix = burger.Prix,
                Description = burger.Description ?? string.Empty,
                Image = burger.Image ?? string.Empty,
                Complements = complements
            };
        }

        // ============================
        // DÉTAIL MENU
        // ============================
        public async Task<MenuViewModel?> GetMenuDetailAsync(int id)
        {
            return await _context.Menus
                .Where(m => m.Id == id && !m.Archived)
                .Include(m => m.Burger)
                .Include(m => m.Frite)
                .Include(m => m.Boisson)
                .Select(m => new MenuViewModel
                {
                    Id = m.Id,
                    Nom = m.Nom,
                    Burger = new BurgerViewModel
                    {
                        Id = m.Burger!.Id,
                        Nom = m.Burger.Nom,
                        Prix = m.Burger.Prix,
                        Description = m.Burger.Description ?? string.Empty,
                        Image = m.Burger.Image ?? string.Empty
                    },
                    Frite = m.Frite != null ? new ComplementViewModel
                    {
                        Id = m.Frite.Id,
                        Nom = m.Frite.Nom,
                        Prix = m.Frite.Prix,
                        Type = m.Frite.Type
                    } : null,
                    Boisson = m.Boisson != null ? new ComplementViewModel
                    {
                        Id = m.Boisson.Id,
                        Nom = m.Boisson.Nom,
                        Prix = m.Boisson.Prix,
                        Type = m.Boisson.Type
                    } : null,
                    Prix = m.Prix,
                    Image = m.Image ?? string.Empty
                })
                .FirstOrDefaultAsync();
        }

        // ============================
        // TOUS LES COMPLÉMENTS
        // ============================
        public async Task<List<ComplementViewModel>> GetComplementsAsync()
        {
            return await _context.Complements
                .Where(c => !c.Archived)
                .Select(c => new ComplementViewModel
                {
                    Id = c.Id,
                    Nom = c.Nom,
                    Prix = c.Prix,
                    Type = c.Type
                })
                .ToListAsync();
        }

        // ============================
        // BURGERS LES PLUS POPULAIRES (CORRIGÉ)
        // ============================
        public async Task<List<BurgerViewModel>> GetPopularBurgersAsync(int limit = 4)
        {
        var burgers = await _context.Burgers
            .Where(b => !b.Archived)
            .Include(b => b.CommandeItems)
            .ToListAsync(); // ✅ async ici

        return burgers
            .OrderByDescending(b => b.CommandeItems!.Count)
            .Take(limit)
            .Select(b => new BurgerViewModel
            {
                Id = b.Id,
                Nom = b.Nom,
                Prix = b.Prix,
                Description = b.Description ?? string.Empty,
                Image = b.Image ?? string.Empty
            })
            .ToList();
        }
        

    // ============================
// MENUS LES PLUS POPULAIRES (CORRIGÉ)
// ============================
    public async Task<List<MenuViewModel>> GetPopularMenusAsync(int limit = 4)
    {
        var menus = await _context.Menus
            .Where(m => !m.Archived)
            .Include(m => m.CommandeItems)
            .Include(m => m.Burger)
            .Include(m => m.Frite)
            .Include(m => m.Boisson)
            .ToListAsync(); // ✅ async ici

        return menus
            .OrderByDescending(m => m.CommandeItems!.Count)
            .Take(limit)
            .Select(m => new MenuViewModel
            {
                Id = m.Id,
                Nom = m.Nom,
                Burger = new BurgerViewModel
                {
                    Id = m.Burger!.Id,
                    Nom = m.Burger.Nom,
                    Prix = m.Burger.Prix,
                    Description = m.Burger.Description ?? string.Empty,
                    Image = m.Burger.Image ?? string.Empty
                },
                Frite = m.Frite != null ? new ComplementViewModel
                {
                    Id = m.Frite.Id,
                    Nom = m.Frite.Nom,
                    Prix = m.Frite.Prix,
                    Type = m.Frite.Type
                } : null,
                Boisson = m.Boisson != null ? new ComplementViewModel
                {
                    Id = m.Boisson.Id,
                    Nom = m.Boisson.Nom,
                    Prix = m.Boisson.Prix,
                    Type = m.Boisson.Type
                } : null,
                Prix = m.Prix,
                Image = m.Image ?? string.Empty
            })
            .ToList();
        }
    }
}