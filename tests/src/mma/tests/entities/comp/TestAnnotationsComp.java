package mma.tests.entities.comp;

import arc.math.geom.*;
import arc.util.*;
import mindustry.annotations.*;
import mindustry.annotations.Annotations.*;
import mindustry.entities.EntityCollisions.*;
import mindustry.gen.*;
import mma.annotations.ModAnnotations.*;
import mma.gen.*;

import static mindustry.Vars.*;

@Annotations.EntityDef({TestAnnotationsc.class})
@Annotations.Component
abstract class TestAnnotationsComp implements Entityc, Velc{
    public Vec2 vec2Test;
    Unit testUnit;

    @Override
    @Annotations.MethodPriority(-1_000_000_000)
//    @UseOnlyImplementation({Velc.class})
//    @IgnoreImplementation({Minerc.class,Unitc.class,Commanderc.class})
//    @Replace
    @GlobalReturn
    public void update(){
        if(testUnit == null){
            return;
        }
    }


    @Override
    @ReplaceInternalImpl
    public boolean serialize(){
        return Time.time>10;
    }

    @SuperMethod(parentName = "update")
    abstract void superUpdate();

    @Replace
    public void move(float cx, float cy){
    }
    @Override
    @Annotations.MethodPriority(1000)
    public void add(){
        throw new RuntimeException();
    }

    @Override
    @Annotations.MethodPriority(1000)
    public void remove(){
        throw new RuntimeException();
    }

    public void test(){

    }

    class AnyCompClass{

    }
}

@Struct
class TestStructStruct{
    short block, floor, overlay;
}