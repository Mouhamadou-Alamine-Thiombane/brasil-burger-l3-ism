using BrasilBurger.Data;
using BrasilBurger.Services;
using BrasilBurger.Models;
using BrasilBurger.Controllers;
using Microsoft.EntityFrameworkCore;

// SOLUTION CRITIQUE pour PostgreSQL et DateTime
AppContext.SetSwitch("Npgsql.EnableLegacyTimestampBehavior", true);

var builder = WebApplication.CreateBuilder(args);

// =====================================
// MVC
// =====================================
builder.Services.AddControllersWithViews();

// =====================================
// PostgreSQL (Neon) avec retry
// =====================================
builder.Services.AddDbContext<ApplicationDbContext>(options =>
    options.UseNpgsql(
        builder.Configuration.GetConnectionString("DefaultConnection"),
        npgsqlOptions =>
        {
            npgsqlOptions.EnableRetryOnFailure(
                maxRetryCount: 5,
                maxRetryDelay: TimeSpan.FromSeconds(10),  // CORRECTION: deux-points ici
                errorCodesToAdd: null
            );
        }
    )
);

// =====================================
// Services
// =====================================
builder.Services.AddScoped<AuthService>();
builder.Services.AddScoped<CompteController>();
builder.Services.AddScoped<BurgerService>();
builder.Services.AddScoped<CommandeService>();
builder.Services.AddScoped<PaiementService>();
builder.Services.AddScoped<SessionService>();

// =====================================
// Session
// =====================================
builder.Services.AddDistributedMemoryCache();
builder.Services.AddSession(options =>
{
    options.IdleTimeout = TimeSpan.FromMinutes(30);
    options.Cookie.HttpOnly = true;
    options.Cookie.IsEssential = true;
});

// =====================================
// HttpContext
// =====================================
builder.Services.AddHttpContextAccessor();

   var port = Environment.GetEnvironmentVariable("PORT") ?? "5000";
   builder.WebHost.UseUrls($"http://0.0.0.0:{port}");

var app = builder.Build();

// =====================================
// TEST CONNEXION DB (TEMPORAIRE AU DEMARRAGE)
// =====================================
using (var scope = app.Services.CreateScope())
{
    var db = scope.ServiceProvider.GetRequiredService<ApplicationDbContext>();
    try
    {
        db.Database.OpenConnection();
        Console.WriteLine("✅ Connexion PostgreSQL OK");
    }
    catch (Exception ex)
    {
        Console.WriteLine($"❌ Impossible de se connecter à la DB : {ex.Message}");
    }
    finally
    {
        db.Database.CloseConnection();
    }
}

// =====================================
// Pipeline HTTP
// =====================================
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Home/Error");
    app.UseHsts();
}

app.UseHttpsRedirection();
app.UseStaticFiles();

app.UseRouting();
app.UseSession();      // ⚠️ AVANT Authorization
app.UseAuthorization();

app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Home}/{action=Index}/{id?}");

app.Run();