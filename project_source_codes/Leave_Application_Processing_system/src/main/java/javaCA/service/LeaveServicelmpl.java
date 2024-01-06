package javaCA.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javaCA.model.Leave;
import javaCA.repository.LeaveRepository;

@Service
@Transactional(readOnly = true)
public class LeaveServicelmpl implements LeaveService {
    @Autowired
    private LeaveRepository leaveRepository;

    @Override
    public List<Leave> findAllLeaves() {
        return leaveRepository.findAll();
    }

    @Override
    @Transactional(readOnly = false)
    public Leave createLeave(Leave leave) {
        return leaveRepository.saveAndFlush(leave);
    }

    @Override
    public Leave findLeave(Integer lid) {
        return leaveRepository.findById(lid).orElse(null);
    }

    @Override
    public Optional<Leave> findLeave(int leaveId) {
        return leaveRepository.findById(leaveId);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteLeave(int leaveId) {
        leaveRepository.deleteById(leaveId);
    }

    @Override
    @Transactional(readOnly = false)
    public Leave changeLeave(Leave leave) {
        return leaveRepository.saveAndFlush(leave);// 参数leave会把老leave覆盖掉
    }

    @Override
    public void removeLeave(Integer lid) {
        leaveRepository.deleteById(lid);
    }

    @Override
    public List<Leave> findPendingLeaveByEID(String eid) {
        return leaveRepository.findPendingLeaveByEID(eid);
    }

    @Override
    public List<Leave> findLeaveByEID(String id) {
        return leaveRepository.findLeaveByEID(id);
    }

    @Override
    public List<Leave> findDecidedLeaveByEID(String eid) {
        return leaveRepository.findDecidedLeaveByEID(eid);
    }

    @Override
    @Transactional(readOnly = false)
    public Leave saveLeave(Leave leave) {
        return leaveRepository.saveAndFlush(leave);
    }
}