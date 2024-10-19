package edu.luc.etl.cs313.android.shapes.model;

import java.util.List;

/**
 * A shape visitor for calculating the bounding box, that is, the smallest
 * rectangle containing the shape. The resulting bounding box is returned as a
 * rectangle at a specific location.
 */
public class BoundingBox implements Visitor<Location> {

    // TODO entirely your job (except onCircle)

    @Override
    public Location onCircle(final Circle c) {
        final int radius = c.getRadius();
        return new Location(-radius, -radius, new Rectangle(2 * radius, 2 * radius));
    }

    @Override
    public Location onFill(final Fill f) {
        return f.getShape().accept(this);
    }

    @Override
    public Location onGroup(final Group g) {
        List<? extends Shape> shapes = g.getShapes();
        int max_x = 0;
        int max_y = 0;
        int min_x = Integer.MAX_VALUE;
        int min_y = Integer.MAX_VALUE;
        for (Shape shape : shapes) {
            Location s = shape.accept(this);
            Shape r = s.getShape();
            int sX = s.getX();
            int sY = s.getY();
            int width = 0;
            int height = 0;
            if (r instanceof Rectangle){
                width = ((Rectangle) r).getWidth();
                height = ((Rectangle) r).getHeight();

            }
            if (sX + width > max_x){max_x=sX + width;}
            if (sY+ height > max_y){max_y=sY + height;}
            if (sX < min_x){min_x=sX;}
            if (sY <min_y){min_y=sY;}

        }
        return new Location(min_x,min_y,new Rectangle(max_x-min_x,max_y-min_y));
    }

    @Override
    public Location onLocation(final Location l) {
        final var s = l.getShape().accept(this);
        s.setX((s.getX()+l.getX()));
        s.setY((s.getY()+l.getY()));
        return s;
    }

    @Override
    public Location onRectangle(final Rectangle r) {
        return new Location(0,0,r);
    }

    @Override
    public Location onStrokeColor(final StrokeColor c) {
        return c.getShape().accept(this);
    }

    @Override
    public Location onOutline(final Outline o) {
        return o.getShape().accept(this);
    }

    @Override
    public Location onPolygon(final Polygon s) {
        final List<? extends Point> points = s.getPoints();
        int min_x = points.get(0).getX();
        int min_y = points.get(0).getY();
        int max_x = 0;
        int max_y = 0;
        for (Point p: points) {
            if (p.getX()<min_x) {min_x = p.getX();}
            if (p.getY()<min_y) {min_y = p.getY();}
            if (p.getX()>max_x) {max_x = p.getX();}
            if (p.getY()>max_y) {max_y = p.getY();}
        }
        return new Location(min_x,min_y,new Rectangle(max_x-min_x,max_y-min_y));
    }
}
