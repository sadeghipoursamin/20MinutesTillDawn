package com.example.Models.enums;

import com.example.Models.App;

import java.util.Objects;

public enum Language {
    // Authentication
    SignUp("Sign Up", "S'inscrire"),
    SignUpMenu("Sign up menu", "Menu d'inscription"),
    Login("Login", "Connexion"),
    LoginMenu("Login menu", "Menu d'inscription"),
    Username("Username: ", "Nom d'utilisateur: "),
    ResetPasswordTitle("Reset Password!", "Réinitialiser le mot de passe !"),
    NewPasswordLabel("New Password:", "Nouveau mot de passe :"),
    RepeatPasswordLabel("Repeat Password:", "Répétez le mot de passe :"),
    ResetButton("Reset", "Réinitialiser"),
    ResetErrorLabel("Invalid password. Please try again.", "Mot de passe invalide. Veuillez réessayer."),
    Password("Password: ", "Mot de passe: "),
    ForgotPassword("Forgot Password?", "Mot de passe oublié ?"),
    ConfirmPassword("Confirm Password", "Confirmer le mot de passe"),
    ResetUsernameTitle("Reset Username!", "Réinitialiser le nom d'utilisateur !"),
    NewUsernameLabel("New Username:", "Nouveau nom d'utilisateur :"),
    GuestLogin("Play as Guest", "Jouer en tant qu'invité"),
    Logout("Logout", "quitter"),
    SecurityQuestion("What's your favorite music band?", "Quelle est votre musique prefere?"),

    // Menus
    Start("Start", "Commencer"),
    Quit("Quit", "Quitter"),
    MainMenu("Main Menu", "Menu principal"),
    Settings("Settings", "Paramètres"),
    Profile("Profile", "Profil"),
    Hint("Hint", "Indice"),
    Pause("Pause", "Pause"),
    Play("Play", "Jouer"),
    Resume("Resume", "Reprendre"),
    GiveUp("Give Up", "Abandonner"),
    Continue("Continue", "Continuer"),
    SaveGame("Save Game", "Sauvegarder"),
    LoadGame("Load Game", "Charger"),
    Language("Language: English", "Langue : Français"),

    // Pre-game setup
    SelectCharacter("Select Character", "Choisir un personnage"),
    SelectWeapon("Select Weapon", "Choisir une arme"),
    SelectDuration("Select Game Duration", "Choisir la durée de jeu"),
    StartGame("Start Game", "Démarrer la partie"),

    // Profile settings
    ChangeUsername("Change Username", "Changer le nom d'utilisateur"),
    ChangePassword("Change Password", "Changer le mot de passe"),
    DeleteAccount("Delete Account", "Supprimer le compte"),
    ChangeAvatar("Change Avatar", "Changer d'avatar"),
    DropAvatarHere("Drop Avatar Here", "Déposez l'avatar ici"),

    // In-game UI
    Health("Health", "Santé"),
    TimeLeft("Time Left", "Temps restant"),
    AmmoLeft("Ammo Left", "Munitions restantes"),
    Kills("Kills", "Éliminations"),
    Level("Level", "Niveau"),
    XPBar("XP Progress", "Progression XP"),

    // End game
    GameOver("Game Over", "Jeu terminé"),
    Victory("Victory", "Victoire"),
    Defeat("Defeat", "Défaite"),
    FinalScore("Final Score", "Score final"),
    Retry("Retry", "Réessayer"),

    // Leaderboard
    Leaderboard("Leaderboard", "Classement"),
    SortByScore("Sort by Score", "Trier par score"),
    SortByKills("Sort by Kills", "Trier par éliminations"),
    SortByUsername("Sort by Username", "Trier par nom d'utilisateur"),
    SortBySurvivalTime("Sort by Survival Time", "Trier par temps de survie"),

    // Audio & visuals
    MusicVolume("Music Volume", "Volume de la musique"),
    BackMusic("Main Music", "Musique principale"),
    SoundEffects("Sound Effects: ", "Effects de la musique: "),
    ChangeMusic("Change Music", "Changer la musique"),
    SFXToggle("Toggle SFX", "Activer/Désactiver SFX"),
    AutoReload("Auto Reload", "Rechargement automatique"),
    BlackAndWhite("Black and White Mode", "Mode noir et blanc"),

    // Tutorial & hints
    ShowKeyHints("Show Key Hints", "Afficher les touches"),
    ShowAbilityInfo("Show Ability Info", "Afficher les capacités"),
    ShowHeroTips("Show Hero Tips", "Astuces pour les héros"),
    ShowCheatCodes("Show Cheat Codes", "Afficher les codes de triche"),

    // Abilities / talents
    ChooseAbility("Choose an Ability", "Choisir une capacité"),
    SelectRandomAbility("Random Ability", "Capacité aléatoire"),
    SelectFromAbilities("Select From 3 Abilities", "Choisir parmi 3 capacités"),
    LevelUp("Level Up", "Niveau supérieur"),

    // Controls
    MoveUp("Up", "Monter"),
    MoveDown("Down", "Descendre"),
    MoveLeft("Left", "Gauche"),
    MoveRight("Right", "Droite"),
    Shoot("Shoot", "Tirer"),
    Space("Space", "L'espace"),
    Reload("Reload", "Recharger"),
    ChangeUsernameButton("Change Username", "Changer le nom d'utilisateur"),
    ChangePasswordButton("Change Password", "Changer le mot de passe"),
    DeleteAccountButton("Delete Account", "Supprimer le compte"),
    ChooseAvatarButton("Choose Avatar", "Choisir un avatar"),
    ToggleAutoAim("Toggle Auto-Aim", "Activer/Désactiver visée auto"),

    // Character state
    TakeDamage("Take Damage", "Subir des dégâts"),
    Invincible("Invincible", "Invincible"),
    Died("Died", "Mort"),

    // Enemies
    SpawnEnemies("Spawn Enemies", "Générer des ennemis"),
    EnemiesLeft("Enemies Left", "Ennemis restants"),
    BossIncoming("Boss Incoming", "Boss en approche"),

    // Game events
    WaveSurvived("Wave Survived", "Vague survécue"),
    NextWave("Next Wave", "Vague suivante"),
    TimeExpired("Time Expired", "Temps écoulé"),
    GamePaused("Game Paused", "Jeu en pause"),
    GameResumed("Game Resumed", "Jeu repris"),

    // Save system
    Saving("Saving...", "Sauvegarde..."),
    SavedSuccessfully("Saved Successfully", "Sauvegardé avec succès"),
    LoadSuccessful("Game Loaded", "Partie chargée"),

    // Debug & cheats
    CheatMode("Cheat Mode", "Mode triche"),
    AddXP("Add XP", "Ajouter de l'XP"),
    AddHealth("Add Health", "Ajouter de la santé"),
    TriggerBoss("Trigger Boss Fight", "Déclencher le boss"),
    ReduceTime("Reduce Time", "Réduire le temps"),
    UnlockAll("Unlock All", "Tout débloquer"),

    // Common
    Confirm("Confirm", "Confirmer"),
    Cancel("Cancel", "Annuler"),
    OK("OK", "OK"),
    Error("Error", "Erreur"),
    Warning("Warning", "Avertissement"),
    MoveUpLabel("Up:", "Haut :"),
    MoveDownLabel("Down:", "Bas :"),
    MoveLeftLabel("Left:", "Gauche :"),
    MoveRightLabel("Right:", "Droite :"),
    ShootLabel("Auto Shoot:", "Tir automatique :"),
    ReloadLabel("Reload:", "Recharger :"),

    GameSettings("Game Settings", "Paramètres du jeu"),
    AutoReloadLabel("Auto-Reload:", "Rechargement automatique :"),
    GrayscaleLabel("Grayscale Mode:", "Mode gris :"),
    LightHaloLabel("Light Halo:", "Halo lumineux :"),
    Back("Back", "Retour"),
    Info("Info", "Info");

    private final String english;
    private final String french;

    Language(String english, String french) {
        this.english = english;
        this.french = french;
    }

    public String getText() {
        return Objects.equals(App.getLanguage(), "en") ? english : french;
    }
}

