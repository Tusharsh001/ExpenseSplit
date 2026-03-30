package com.tushar.split.Service;


import com.tushar.split.DTO.UserSummary;
import com.tushar.split.DTO.expense.ExpenseRequest;
import com.tushar.split.DTO.expense.ExpenseResponse;
import com.tushar.split.DTO.expense.SplitSummary;
import com.tushar.split.Model.Expense;
import com.tushar.split.Model.ExpenseSplit;
import com.tushar.split.Model.SplitGroups;
import com.tushar.split.Model.Users;
import com.tushar.split.Repo.ExpenseRepo;
import com.tushar.split.Repo.ExpenseSplitRepo;
import com.tushar.split.Repo.GroupRepo;
import com.tushar.split.Repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExpenseService {

    @Autowired
    private ExpenseSplitRepo exSplitRepo;
    @Autowired
    private ExpenseRepo expenseRepo;
    @Autowired
    private GroupRepo groupRepo;
    @Autowired
    private UserRepo userRepo;

    public ExpenseResponse createExpense(ExpenseRequest request,String username){

        // 1.get Current User

        Users currentUser=getUserByUsername(username);

        // 2. get Group and validate memberShip
        SplitGroups group =groupRepo.findById(request.getGroupId()).orElseThrow(
                ()-> new RuntimeException("Group Not Found ")
        );

        boolean isMember=group.getMembers().stream()
                .anyMatch(m -> m.getId()==currentUser.getId());
         if(!isMember){
             throw new RuntimeException("You are not A Member of this Group");
         }


         //3. get all payer and validate
        // this for the case if any other member of the group create a rquest for someone else

          Users paidBy =userRepo.findById(request.getPaidById()).orElseThrow(
                  () -> new RuntimeException("User Not Found")
          );

        boolean isPayerInGroup = group.getMembers().stream()
                .anyMatch(m -> m.getId()==paidBy.getId());
        if (!isPayerInGroup) {
            throw new RuntimeException("Payer is not a member of this group");
        }



        // 4. create expense Entity

        Expense expense=new Expense();

         expense.setDescription(request.getDescription());
         expense.setAmount(request.getAmount());
         expense.setCreatedAt(LocalDateTime.now());
         expense.setGroup(group);
         expense.setPaidBy(paidBy);
         expense.setSplitType(request.getSplitType());

         ///  5. main logic for balance calculation on the bases of splitType

        List <ExpenseSplit> splits=calculateSplits(expense,request,group);

        expense.setSplits(splits);

        ///  save the repo
        Expense savedExpense=expenseRepo.save(expense);

        log.info("Expense Crated : {} in group {} by {} " ,savedExpense.getDescription(),group.getName(),paidBy.getUsername() );

        ///  last and the final return the expense Response

        return mapToResponse(savedExpense,currentUser);

    }


    public ExpenseResponse getExpenseById(int expenseId, String username) {

    }

    public List<ExpenseResponse> getGroupExpenses(int groupId, String username) {

    }


    public List<ExpenseResponse> getMyExpenses(String username) {
    }

    public void deleteExpense(int expenseId, String username) {
    }

    public Map<Long, BigDecimal> calculateGroupBalances(int groupId, String username) {
    }

    public List<SettlementResponse> getSuggestedSettlements(int groupId, String username) {
    }


    ///---------------------HELPER METHODS========================================

    private Users getUserByUsername(String username){
        return userRepo.findByUsername(username).orElseThrow(
                ()-> new RuntimeException("User Not Found")
        );
    }

    private Users getUserById(int userId){
        return userRepo.findById(userId).orElseThrow(
                ()-> new RuntimeException("User Not Found")
        );
    }
    private boolean isUserInGroup(SplitGroups group, Users user) {
        return group.getMembers().stream()
                .anyMatch(m -> m.getId()==user.getId());
    }

    private List<ExpenseSplit> calculateSplits(Expense expense, ExpenseRequest request, SplitGroups group) {

        List<Users> members=group.getMembers();

        List<ExpenseSplit> splits=new ArrayList<>();

        BigDecimal totalAmount=request.getAmount();

        switch (request.getSplitType()){

            case EQUAL :
                BigDecimal perPerson =totalAmount
                        .divide(BigDecimal.valueOf(members.size()), 2, RoundingMode.HALF_UP);;

                for(Users user: members){
                    ExpenseSplit split=new ExpenseSplit();
                    split.setExpense(expense);
                    split.setUser(user);
                    split.setAmount(perPerson);
                    splits.add(split);
                }
                break;

            case PERCENTAGE:
                Map<Integer, BigDecimal> percentage=request.getSplitDetails();


                BigDecimal totalPercentage=BigDecimal.ZERO;

                for(BigDecimal percen : percentage.values()){
                      totalPercentage=totalPercentage.add(percen);
                }

                if(totalPercentage.compareTo(BigDecimal.valueOf(100))!=0){
                    throw new RuntimeException("Percentage must sum to 100");
                }

                for(Map.Entry<Integer,BigDecimal> entry: percentage.entrySet()){
                    int userId=entry.getKey();
                    BigDecimal percent=entry.getValue();
                    Users user=userRepo.findById(userId).orElseThrow(
                            () -> new RuntimeException("User not Found")
                    );

                    if(!isUserInGroup(group,user)){
                        throw new RuntimeException("User Not in the group");
                    }

                    BigDecimal amount=totalAmount
                            .multiply(percent)
                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);;

                    ExpenseSplit split=new ExpenseSplit();
                    split.setExpense(expense);
                    split.setUser(user);
                    split.setAmount(amount);
                    splits.add(split);

                }

                break;

            case EXACT:
                Map<Integer,BigDecimal> exactAmount=request.getSplitDetails();
                BigDecimal totalExact = BigDecimal.ZERO;
                for (BigDecimal amount : exactAmount.values()) {
                    totalExact = totalExact.add(amount);
                }
                if (totalExact.compareTo(totalAmount) != 0) {
                    throw new RuntimeException("Exact amounts must sum to total expense amount");
                }
                for(Map.Entry<Integer,BigDecimal> entry: exactAmount.entrySet()){

                    int Iduser= entry.getKey();
                    BigDecimal amount = entry.getValue();

                    Users users = userRepo.findById(Iduser)
                            .orElseThrow(() -> new RuntimeException("User not found: " ));

                    if (!isUserInGroup(group, users)) {
                        throw new RuntimeException("User not in group: " + users.getUsername());
                    }

                    ExpenseSplit split=new ExpenseSplit();
                    split.setExpense(expense);
                    split.setUser(users);
                    split.setAmount(amount);
                    splits.add(split);
                }
                break;
        }
        return splits;
    }

    private ExpenseResponse mapToResponse(Expense expense,Users currentUser){

        BigDecimal youShare =BigDecimal.ZERO;

        for(ExpenseSplit split: expense.getSplits()){
            if(split.getUser().getId()== currentUser.getId()){
                youShare=split.getAmount();
                break;
            }
        }

        return ExpenseResponse.builder()
                .id(expense.getId())
                .description(expense.getDescription())
                .amount(expense.getAmount())
                .createdAt(expense.getCreatedAt())
                .groupId(expense.getGroup().getId())
                .paidBy(UserSummary.builder()
                        .id(expense.getPaidBy().getId())
                        .username(expense.getPaidBy().getUsername())
                        .email(expense.getPaidBy().getEmail())
                        .build())
                .splits(expense.getSplits().stream()
                        .map(s -> SplitSummary.builder()
                                .userId(s.getUser().getId())
                                .username(s.getUser().getUsername())
                                .amount(s.getAmount())
                                .percentage(s.getPercentage())
                                .build())
                        .collect(Collectors.toList()))
                .splitType(expense.getSplitType())
                .yourShare(youShare)
                .build();


    }



}
