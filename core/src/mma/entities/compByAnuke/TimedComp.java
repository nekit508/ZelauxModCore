package mma.entities.compByAnuke;

import arc.math.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.gen.*;
import static mindustry.logic.LAccess.*;

@mma.annotations.ModAnnotations.Component
abstract class TimedComp implements Entityc, Scaled {

    float time, lifetime;

    // called last so pooling and removal happens then.
    @mma.annotations.ModAnnotations.MethodPriority(100)
    @Override
    public void update() {
        time = Math.min(time + Time.delta, lifetime);
        if (time >= lifetime) {
            remove();
        }
    }

    @Override
    public float fin() {
        return time / lifetime;
    }
}
