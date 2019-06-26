package com.sharding.client.router;

import java.util.Iterator;
import java.util.Set;

/**
 * @Description: 路由结果实体类
 * @Auther: kun
 * @Date: 2019-04-03 10:40
 */
public class RouterUnit {

    private Set<String> dataNodes;
    private boolean isFinishedRoute;

    public Set<String> getDataNodes() {
        return dataNodes;
    }

    public void setDataNodes(Set<String> dataNodes) {
        this.dataNodes = dataNodes;
    }

    public void setFinishedRoute(boolean finishedRoute) {
        isFinishedRoute = finishedRoute;
    }

    public boolean isFinishedRoute() {
        return isFinishedRoute;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("route={");
        if (dataNodes != null) {
            Iterator it = dataNodes.iterator();
            while (it.hasNext()) {
                s.append("\n ");
                s.append(" -> ").append(it.next());
            }
        }
        s.append("\n}");
        return s.toString();
    }
}
