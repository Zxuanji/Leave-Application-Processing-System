package javaCA.service;

import java.util.List;
import java.util.Optional;

import javaCA.model.Leave;

public interface LeaveService {

    List<Leave> findAllLeaves();

    Leave findLeave(Integer lid);

    Optional<Leave> findLeave(int leaveId);

    Leave createLeave(Leave leave);

    Leave changeLeave(Leave leave);

    void deleteLeave(int leaveId);

    void removeLeave(Integer lid);

    public List<Leave> findPendingLeaveByEID(String eid);

    public List<Leave> findLeaveByEID(String id);

    List<Leave> findDecidedLeaveByEID(String eid);

    public Leave saveLeave(Leave leave);
}
