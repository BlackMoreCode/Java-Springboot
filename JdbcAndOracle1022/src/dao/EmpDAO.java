package dao;

import common.Common;
import vo.EmpVO;

import javax.xml.transform.Result;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// DAO (Database Access Object) : 데이터베이스에 접근하여 데이터를 조회하거나 수정하는데
// DAO는 VO객체와 데이터베이스 간의 매핑을 담당.
public class EmpDAO {
    Connection conn = null;
    Statement stmt = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    Scanner sc = new Scanner(System.in);

    public EmpDAO() {
        sc = new Scanner(System.in);
    }

    // SELECT 기능 (조회) 구현
    public List<EmpVO> empSelect() {
        List<EmpVO> list = new ArrayList<>();
        try {
            conn = Common.getConnection(); // 오라클DB 연결
            stmt = conn.createStatement(); // Statement 생성
            String query = "SELECT * FROM EMP"; //..다만 이건 하드코딩이다..
            // executeQuery: select 문과 같이 결과값이 여러 개의 레코드로 반환되는 경우 사용
            rs = stmt.executeQuery(query); // ResultSet: 여러 행의 결과값을 받아서 반복자(iterator)를 제공
            // next() : 현재 행에서 한 행 앞으로 이동
            // previous(): 현재 행에서 한 행 뒤로 이동
            // first() : 첫번째 행으로 이동
            // last() : 마지막 행으로 이동
            while (rs.next()) {
                int empNo = rs.getInt("EMPNO");
                String name = rs.getString("ENAME");
                String job = rs.getString("JOB");
                int mgr = rs.getInt("MGR");
                Date date = rs.getDate("HIREDATE");
                BigDecimal sal = rs.getBigDecimal("SAL");
                BigDecimal comm = rs.getBigDecimal("COMM");
                int deptNo = rs.getInt("DEPTNO");
                EmpVO vo = new EmpVO(empNo, name, job, mgr, date, sal, comm, deptNo);
                list.add(vo);
            }
            Common.close(rs);
            Common.close(stmt);
            Common.close(conn);

        } catch (Exception e) {
            System.out.println("EMP 조회 실패");
        }
        return list;
    }

    // INSERT 구현
    public boolean empInsert(EmpVO vo) {
        String sql = "INSERT INTO EMP(EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, COMM, DEPTNO) VALUES (?,?,?,?,?,?,?,?)";

        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(sql);
            psmt.setInt(1, vo.getEmpNO());
            psmt.setString(2, vo.getName());
            psmt.setString(3, vo.getJob());
            psmt.setInt(4, vo.getMgr());
            psmt.setDate(5, vo.getDate());
            psmt.setBigDecimal(6, vo.getSal());
            psmt.setBigDecimal(7, vo.getComm());
            psmt.setInt(8, vo.getDeptNo());
            int rst = psmt.executeUpdate(); // INSERT, UPDATE, DELETE에 해당하는 함수
            System.out.println("INSERT 결과로 영향 받는 행의 갯수 : " + rst);
            return true; // 원래는 반환값 받는거 처리해야한다.. 쿼리문에 대한 성공실패만 판정
        } catch (Exception e) {
            System.out.println("INSERT 실패");
            return false;
        } finally {
            Common.close(rs);
            Common.close(stmt);
            Common.close(conn);
        }
    }

    public boolean empDelete(EmpVO vo) {
        String sql = "DELETE FROM EMP WHERE ENAME = ?";

        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(sql);
            psmt.setString(1, vo.getName());
            int rst = psmt.executeUpdate(); // INSERT, UPDATE, DELETE에 해당하는 함수
            System.out.println("DELETE 결과로 영향 받는 행의 갯수 : " + rst);
            return true;
        } catch (Exception e) {
            System.out.println("DELETE 실패");
            return false;
        } finally {
            Common.close(rs);
            Common.close(stmt);
            Common.close(conn);
        }
    }

    public boolean empUpdate(EmpVO vo) {
        String sql = "UPDATE EMP SET JOB = ?, SAL = ?, COMM = ? WHERE ENAME = ?";
        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(sql);
            psmt.setString(1, vo.getJob());
            psmt.setBigDecimal(2, vo.getSal());
            psmt.setBigDecimal(3,  vo.getComm());
            psmt.setString(4, vo.getName());
            int rst = psmt.executeUpdate(); // INSERT, UPDATE, DELETE에 해당하는 함수
            System.out.println("UPDATE 결과로 영향 받는 행의 갯수 : " + rst);
            return true;
        } catch (Exception e) {
            System.out.println("UPDATE 실패");
            return false;
        } finally {
            Common.close(psmt);
            Common.close(conn);
        }
    }


    public void empSelectResult(List<EmpVO> list) {
        System.out.println("--------------------------------------------------------");
        System.out.println("                사원정보");
        System.out.println("--------------------------------------------------------");
        for(EmpVO e : list) {
            System.out.print(e.getEmpNO() + " ");
            System.out.print(e.getName() + " ");
            System.out.print(e.getJob() + " ");
            System.out.print(e.getMgr() + " ");
            System.out.print(e.getDate() + " ");
            System.out.print(e.getSal() + " ");
            System.out.print(e.getComm() + " ");
            System.out.print(e.getDeptNo());
            System.out.println();
        }
        System.out.println("--------------------------------------------------------");
    }

}
