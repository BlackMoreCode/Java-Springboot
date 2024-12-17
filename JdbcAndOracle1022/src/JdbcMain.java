import dao.EmpDAO;
import vo.EmpVO;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class JdbcMain {
    public static void main(String[] args) {
        menuSelect();
    }
    public static void menuSelect() {
        Scanner sc = new Scanner(System.in);
        EmpDAO dao = new EmpDAO();

        while (true) {
            System.out.println("========= EMP TABLE ==========");
            System.out.println("메뉴를 선택 : ");
            System.out.print("[1]SELECT [2]INSERT [3]UPDATE [4]DELETE [5]EXIT : ");
            int choice  = sc.nextInt();
            boolean isSuccess = false;
            switch (choice) {
                case 1:
                    List<EmpVO> list = dao.empSelect();
                    dao.empSelectResult(list);
                    break;
                case 2:
                    isSuccess = dao.empInsert(empInput());
                    if(isSuccess) System.out.println("사원 등록에 성공 했습니다");
                    else System.out.println("사원 등록에 실패 했습니다");
                    break;
                case 3:
                    isSuccess = dao.empUpdate(empUpdate());
                    if(isSuccess) System.out.println("사원 정보 갱신에 성공 했습니다");
                    else System.out.println("사원 정보 갱신에 실패 했습니다");
                    break;
                case 4:
                    isSuccess = dao.empDelete(empDelete());
                    if(isSuccess) System.out.println("사원 제거에 성공 했습니다");
                    else System.out.println("사원 제거에 실패 했습니다");
                    break;
                case 5:
                    System.out.println("프로그램을 종료합니다");
                    return;
            }
        }
    }

    public static EmpVO empInput() {
        Scanner sc = new Scanner(System.in);
        System.out.println("사원 정보를 입력하시오.");
        System.out.print("사원 번호 : ");
        int empNo = sc.nextInt();
        System.out.print("사원 이름 : ");
        String name = sc.next();
        System.out.print("직책 : ");
        String job = sc.next();
        System.out.print("상관 사원 번호 : ");
        int mgr = sc.nextInt();
        System.out.print("입사일 (yyyy-mm-dd) : ");
        String date = sc.next(); // 날짜는 문자열로 입력 받아도 날짝 형식에만 맞으면 된다.
        System.out.print("급여 : ");
        BigDecimal sal = sc.nextBigDecimal();
        System.out.print("성과급 : ");
        BigDecimal comm = sc.nextBigDecimal();
        System.out.print("부서 번호 : ");
        int deptNo = sc.nextInt();
        return new EmpVO(empNo, name, job, mgr, Date.valueOf(date), sal, comm, deptNo);
    }

    public static EmpVO empDelete() {
        Scanner sc = new Scanner(System.in);
        System.out.print("사원 이름 : ");
        String name = sc.next();
        return new EmpVO(name);
    }

    public static EmpVO empUpdate() {
        Scanner sc = new Scanner(System.in);
        System.out.print("변경할 사원 이름 : ");
        String name = sc.next();
        System.out.print("변경할 직책 : ");
        String job = sc.next();
        System.out.print("변경할 급여 : ");
        BigDecimal sal = sc.nextBigDecimal();
        System.out.print("변경할 성과급 : ");
        BigDecimal comm = sc.nextBigDecimal();
        return new EmpVO(name, job, sal, comm);
    }

    public static String empDeleteInput() {
        Scanner sc = new Scanner(System.in);
        System.out.print("삭제할 이름을 입력 하세요 : ");
        return sc.next();
    }

}
