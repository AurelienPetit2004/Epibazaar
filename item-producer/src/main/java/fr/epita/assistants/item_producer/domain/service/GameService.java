package fr.epita.assistants.item_producer.domain.service;

import fr.epita.assistants.item_producer.converter.DataConverter;
import fr.epita.assistants.item_producer.data.model.GameModel;
import fr.epita.assistants.item_producer.data.repository.GameRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@ApplicationScoped
public class GameService {
    @Inject
    DataConverter dataConverter;

    public ArrayList<ArrayList<String>> start(String mapPath)
    {
        dataConverter.clearGame();

        StringBuilder stringBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(mapPath))) {
            stream.forEach(line -> stringBuilder.append(line).append(";"));
        } catch (IOException e) {
            return null;
        }

        if (stringBuilder.length() - 1 <= 0)
            return new ArrayList<>();
        String str = stringBuilder.substring(0, stringBuilder.length() - 1);

        dataConverter.addGame(str);

        str = stringBuilder.toString();
        ArrayList<ArrayList<String>> res = new ArrayList<>();
        ArrayList<String> arrayList = new ArrayList<>();

        int i = 0;
        while (i < str.length())
        {
            if (str.charAt(i) == ';')
            {
                if (arrayList.isEmpty())
                    return null;
                res.add(arrayList);
                arrayList = new ArrayList<>();
                i = i + 1;
            }
            else if (str.charAt(i) < '1' || str.charAt(i) > '9')
                return null;
            else
            {
                int len = str.charAt(i) - '0';
                if (i + 1 == str.length())
                    return null;
                String s;
                if (str.charAt(i + 1) == 'R')
                    s = "ROCK";
                else if (str.charAt(i + 1) == 'W')
                    s = "WOOD";
                else if (str.charAt(i + 1) == 'G')
                    s = "GROUND";
                else if (str.charAt(i + 1) == 'O')
                    s = "WATER";
                else if (str.charAt(i + 1) == 'M')
                    s = "MONEY";
                else
                    return null;
                for (int j = 0; j < len; j++)
                    arrayList.add(s);
                i = i + 2;
            }
        }

        return res;
    }

    public GameModel getGame()
    {
        return dataConverter.getGame();
    }

    public Boolean isRunning()
    {
        return dataConverter.isRunning();
    }

    public ArrayList<ArrayList<String>> getMap()
    {
        GameModel gameModel = this.getGame();
        String str = gameModel.getMap() + ";";

        ArrayList<ArrayList<String>> res = new ArrayList<>();
        ArrayList<String> arrayList = new ArrayList<>();

        int i = 0;
        while (i < str.length())
        {
            if (str.charAt(i) == ';')
            {
                if (arrayList.isEmpty())
                    return null;
                res.add(arrayList);
                arrayList = new ArrayList<>();
                i = i + 1;
            }
            else if (str.charAt(i) < '1' || str.charAt(i) > '9')
                return null;
            else
            {
                int len = str.charAt(i) - '0';
                if (i + 1 == str.length())
                    return null;
                String s;
                if (str.charAt(i + 1) == 'R')
                    s = "ROCK";
                else if (str.charAt(i + 1) == 'W')
                    s = "WOOD";
                else if (str.charAt(i + 1) == 'G')
                    s = "GROUND";
                else if (str.charAt(i + 1) == 'O')
                    s = "WATER";
                else if (str.charAt(i + 1) == 'M')
                    s = "MONEY";
                else
                    return null;
                for (int j = 0; j < len; j++)
                    arrayList.add(s);
                i = i + 2;
            }
        }

        return res;
    }
}
