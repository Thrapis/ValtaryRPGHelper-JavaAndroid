package wa.mobile.rpghelper.modal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import java.util.function.Predicate;

import wa.mobile.rpghelper.R;
import wa.mobile.rpghelper.database.context.DatabaseContextSingleton;
import wa.mobile.rpghelper.database.dao.AbilityDao;
import wa.mobile.rpghelper.database.entity.Ability;

public class AbilityModal {

    public static void Create(Context context, int characterId, Runnable onPositiveComplete) {

        AbilityDao abilityDao = DatabaseContextSingleton
                .getDatabaseContext(context)
                .abilityDao();

        LayoutInflater inflater = LayoutInflater.from(context);
        AlertDialog.Builder modal = new AlertDialog.Builder(context);

        View layout = inflater.inflate(R.layout.modal_add_ability, null);

        modal.setView(layout);
        modal.setPositiveButton(R.string.placer_create, (dialogInterface, i) -> {
            String name = ((EditText) layout.findViewById(R.id.ability_name)).getText().toString();
            String description = ((EditText) layout.findViewById(R.id.ability_description)).getText().toString();
            Ability ability = new Ability(name, characterId, description);
            abilityDao.insert(ability);
            onPositiveComplete.run();
        });
        modal.setNegativeButton(R.string.placer_cancel, (dialogInterface, i) -> {});
        modal.show();
    }

    public static void Update(Context context, int abilityId, Runnable onPositiveComplete) {

        AbilityDao abilityDao = DatabaseContextSingleton
                .getDatabaseContext(context)
                .abilityDao();

        Ability ability = abilityDao.get(abilityId);

        LayoutInflater inflater = LayoutInflater.from(context);
        AlertDialog.Builder modal = new AlertDialog.Builder(context);

        View layout = inflater.inflate(R.layout.modal_add_ability, null);
        ((EditText) layout.findViewById(R.id.ability_name)).setText(ability.getName());
        ((EditText) layout.findViewById(R.id.ability_description)).setText(ability.getDescription());

        modal.setView(layout);
        modal.setPositiveButton(R.string.placer_edit, (dialogInterface, i) -> {
            String name = ((EditText) layout.findViewById(R.id.ability_name)).getText().toString();
            String description = ((EditText) layout.findViewById(R.id.ability_description)).getText().toString();
            ability.setName(name);
            ability.setDescription(description);
            abilityDao.update(ability);
            onPositiveComplete.run();
        });
        modal.setNegativeButton(R.string.placer_cancel, (dialogInterface, i) -> {});
        modal.show();
    }

    public static void Delete(Context context, int abilityId, Runnable onPositiveComplete) {

        AbilityDao abilityDao = DatabaseContextSingleton
                .getDatabaseContext(context)
                .abilityDao();

        Ability ability = abilityDao.get(abilityId);

        AlertDialog.Builder modal = new AlertDialog.Builder(context);

        String message = String.format(context.getString(R.string.question_ability_delete), ability.getName());

        modal.setMessage(message);
        modal.setPositiveButton(R.string.placer_delete, (dialogInterface, i) -> {
            abilityDao.delete(ability);
            onPositiveComplete.run();
        });
        modal.setNegativeButton(R.string.placer_cancel, (dialogInterface, i) -> {});
        modal.show();
    }
}
