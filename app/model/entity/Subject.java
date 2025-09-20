package app.model.entity;

public class Subject {
    private final String id;        // 8 digits; faculty: 0550* and gen-ed: 9069*
    private final String name;
    private final int credits;      // >0
    private final String teacher;   // store only name
    private final String prereqId;  // nullable
    private final int maxEnroll;    // -1 = no limit
    private int currentEnroll;      // >= 0

    public Subject(String id, String name, int credits, String teacher, String prereqId, int maxEnroll, int currentEnroll) {
        this.id = id; 
        this.name = name; 
        this.credits = credits; 
        this.teacher = teacher; 
        this.prereqId = (prereqId == null || prereqId.isBlank()) ? null : prereqId;
        this.maxEnroll = maxEnroll; 
        this.currentEnroll = currentEnroll;
    }

    public String getId() { 
        return id; 
    }

    public String getName() { 
        return name; 
    }

    public int getCredits() { 
        return credits; 
    }

    public String getTeacher() { 
        return teacher; 
    }

    public String getPrereqId() { 
        return prereqId; 
    }

    public int getMaxEnroll() { 
        return maxEnroll; 
    }

    public int getCurrentEnroll() { 
        return currentEnroll; 
    }

    public void incEnroll() { 
        currentEnroll++; 
    }

    public boolean hasCap() { 
        return maxEnroll != -1; 
    }
}
