package csapat.app.teamstructure.model;

import java.util.List;

public class Team {

    private String leader;
    private String name;
    private List<String> patrols;

    public Team() {}

    public Team(String leader, String name, List<String> patrols) {
        this.leader = leader;
        this.name = name;
        this.patrols = patrols;
    }
    public String getLeader() {
        return leader;
    }

    public String getName() {
        return name;
    }

    public List<String> getPatrols() {
        return patrols;
    }
}
