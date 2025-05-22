package com.example.Views;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.example.Main;
import com.example.Models.Player;
import com.example.Models.enums.Ability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UpdatePlayerWindow extends Window {
    private Player player;
    private TextButton ability1Button;
    private TextButton ability2Button;
    private TextButton ability3Button;
    private Label instructionLabel;
    private Label levelLabel;
    private Runnable onComplete;

    private Ability selectedAbility1;
    private Ability selectedAbility2;
    private Ability selectedAbility3;

    public UpdatePlayerWindow(Skin skin, Player player) {
        super("Level Up!", skin);
        this.player = player;

        this.instructionLabel = new Label("Choose an ability to enhance your character:", skin);
        this.levelLabel = new Label("Level " + player.getLevel() + " Reached!", skin, "title");

        List<Ability> randomAbilities = getRandomAbilities();
        selectedAbility1 = randomAbilities.get(0);
        selectedAbility2 = randomAbilities.get(1);
        selectedAbility3 = randomAbilities.get(2);

        // Create buttons with ability names and descriptions
        this.ability1Button = new TextButton(getAbilityDisplayText(selectedAbility1), skin);
        this.ability2Button = new TextButton(getAbilityDisplayText(selectedAbility2), skin);
        this.ability3Button = new TextButton(getAbilityDisplayText(selectedAbility3), skin);

        setupLayout();
        setupListeners();

        // Window properties
        this.setSize(900, 400);
        this.setPosition(
            (com.badlogic.gdx.Gdx.graphics.getWidth() - 600) / 2f,
            (com.badlogic.gdx.Gdx.graphics.getHeight() - 400) / 2f
        );
        this.setModal(true);
        this.setMovable(false);
    }

    private void setupLayout() {
        // Add level label
        this.add(levelLabel).colspan(3).center().padBottom(20);
        this.row();

        // Add instruction
        this.add(instructionLabel).colspan(3).center().padBottom(20);
        this.row();

        // Add ability buttons in a row
        this.add(ability1Button).width(200).height(100).pad(10);
        this.add(ability2Button).width(200).height(100).pad(10);
        this.add(ability3Button).width(200).height(100).pad(10);
        this.row();
    }

    private void setupListeners() {
        ability1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                selectAbility(selectedAbility1);
            }
        });

        ability2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                selectAbility(selectedAbility2);
            }
        });

        ability3Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                selectAbility(selectedAbility3);
            }
        });
    }

    private void selectAbility(Ability ability) {
        player.addAbility(ability);
        player.setChosenAbility(ability);

        applyAbilityEffect(ability);

        if (onComplete != null) {
            onComplete.run();
        }
        this.remove();
    }

    private void applyAbilityEffect(Ability ability) {
        switch (ability) {
            case VITALITY:
                break;
            case DAMAGER:

                break;
            case PROCREASE:
                break;
            case AMOCREASE:
                break;
            case SPEEDY:

                break;
        }
    }

    private List<Ability> getRandomAbilities() {
        List<Ability> allAbilities = new ArrayList<>();
        for (Ability ability : Ability.values()) {
            allAbilities.add(ability);
        }

        Collections.shuffle(allAbilities);
        return allAbilities.subList(0, Math.min(3, allAbilities.size()));
    }

    private String getAbilityDisplayText(Ability ability) {
        switch (ability) {
            case VITALITY:
                return "Vitality";
            case DAMAGER:
                return "Damager";
            case PROCREASE:
                return "Procrease";
            case AMOCREASE:
                return "Amcrease";
            case SPEEDY:
                return "Speed";
            default:
                return ability.name();
        }
    }

    public void setOnComplete(Runnable onComplete) {
        this.onComplete = onComplete;
    }
}
