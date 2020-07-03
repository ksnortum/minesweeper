package minesweeper;

public enum Neighbor {
    NORTH {
        @Override
        public Point getPoint() {
            return new Point(0, -1);
        }
    },
    SOUTH {
        @Override
        public Point getPoint() {
            return new Point(0, 1);
        }
    },
    EAST {
        @Override
        public Point getPoint() {
            return new Point(1, 0);
        }
    },
    WEST {
        @Override
        public Point getPoint() {
            return new Point(-1, 0);
        }
    },
    NORTHEAST {
        @Override
        public Point getPoint() {
            return new Point(1, -1);
        }
    },
    NORTHWEST {
        @Override
        public Point getPoint() {
            return new Point(-1, -1);
        }
    },
    SOUTHEAST {
        @Override
        public Point getPoint() {
            return new Point(1, 1);
        }
    },
    SOUTHWEST {
        @Override
        public Point getPoint() {
            return new Point(-1, 1);
        }
    };

    public abstract Point getPoint();
}
