package com.aristotle.member.ui.account;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.jonatan.contexthelp.ContextHelp;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Membership;
import com.aristotle.core.persistance.MembershipTransaction;
import com.aristotle.core.persistance.User;
import com.aristotle.member.service.MemberService;
import com.aristotle.member.ui.NavigableView;
import com.aristotle.member.ui.util.UiComponentsUtil;
import com.aristotle.member.ui.util.VaadinSessionUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

//@SpringComponent
//@UIScope
@SpringView(name = MembershipView.NAVIAGATION_NAME)
public class MembershipView extends VerticalLayout implements NavigableView{

	private static final long serialVersionUID = 1L;
	public static final String NAVIAGATION_NAME = "membership";


	private VerticalLayout content;

	private Label errorLabel;
	private Label successLabel;
	
	private Label membershipIdLabel;
	private Label membershipStartDateLabel;
	private Label membershipEndDateLabel;
	private Label membershipIdValueLabel;
	private Label membershipStartDateValueLabel;
	private Label membershipEndDateValueLabel;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
	
	private Button feePaymentButton;
	private Table membershipTransactionTable;
	
	@Autowired
	private MemberService memberService;
	@Autowired
	private VaadinSessionUtil vaadinSessionUtil;
	@Autowired
	private UiComponentsUtil uiComponentsUtil;

	private ContextHelp contextHelp;
	private volatile boolean initialized = false;


	public MembershipView() {
	}

	public void init() throws AppException {
		if(!initialized){
			this.setWidth("90%");
			User user = loadUserData();
			System.out.println("User : "+user);
			Membership membership = memberService.getUserMembership(user.getId());
			List<MembershipTransaction> membershipTransactions = memberService.getUserMembershipTransactions(user.getId());
			buildUiScreen(user, membershipTransactions, membership);
			addListeners();
			initialized = true;
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		try{
			init();	
		}catch(Exception ex){
			uiComponentsUtil.setLabelError(errorLabel, ex);
			ex.printStackTrace();
		}
		
	}
	
	private User loadUserData() throws AppException{
		
		User loggedInUser = vaadinSessionUtil.getLoggedInUserFromSession();
		return loggedInUser;
		
	}
	
	
	private void buildUiScreen(User user, List<MembershipTransaction> membershipTransactions, Membership membership){
		this.addStyleName(ValoTheme.LAYOUT_CARD);
		MarginInfo marginInfo = new MarginInfo(true);
		this.setMargin(marginInfo);
		
		contextHelp = new ContextHelp();
		contextHelp.extend(UI.getCurrent());
		contextHelp.setFollowFocus(true);
		
		
		
		errorLabel = uiComponentsUtil.buildErrorlabel();
		successLabel = uiComponentsUtil.buildSuccessLabel();
		
		membershipIdLabel = new Label();
		membershipIdLabel.setId("membershipId");
		membershipIdLabel.setContentMode(ContentMode.HTML);
		membershipIdLabel.setValue("Membership Number/Id");
		
		membershipIdValueLabel = new Label();
		membershipIdValueLabel.setId("membershipValueId");
		membershipIdValueLabel.setContentMode(ContentMode.HTML);
		membershipIdValueLabel.setValue("<b>"+membership.getMembershipId()+"</b>");

		membershipStartDateLabel = new Label();
		membershipStartDateLabel.setId("membershipStartDate");
		membershipStartDateLabel.setValue("Membership Start Date");
		
		membershipStartDateValueLabel = new Label();
		membershipStartDateValueLabel.setId("membershipStartDateValue");
		membershipStartDateValueLabel.setValue(simpleDateFormat.format(membership.getStartDate()));

		membershipEndDateLabel  = new Label();
		membershipEndDateLabel.setId("membershipEndDate");
		membershipEndDateLabel.setValue("Membership Renew Date");
		
		membershipEndDateValueLabel  = new Label();
		membershipEndDateValueLabel.setId("membershipEndDateValue");
		membershipEndDateValueLabel.setValue(simpleDateFormat.format(membership.getStartDate()));
		
		GridLayout gridLayout = new GridLayout(2, 3);
		gridLayout.addStyleName("outlined");
		gridLayout.addComponents(membershipIdLabel, membershipIdValueLabel, membershipStartDateLabel, membershipStartDateValueLabel, membershipEndDateLabel, membershipEndDateValueLabel);
		
		feePaymentButton = uiComponentsUtil.buildButton(ValoTheme.BUTTON_LINK, FontAwesome.MONEY, "Pay Membership Feer(Rs 50)");
		
		Calendar oneMonthAfterToday = Calendar.getInstance();
		oneMonthAfterToday.add(Calendar.MONTH, 1);
		feePaymentButton.setVisible(membership == null || membership.getEndDate().before(oneMonthAfterToday.getTime()));
		
		membershipTransactionTable = new Table();
		IndexedContainer mTransactionIndexedContainer = new IndexedContainer();
		mTransactionIndexedContainer.addContainerProperty("ID", Long.class, null);
		mTransactionIndexedContainer.addContainerProperty("Transaction Date", Date.class, null);
		mTransactionIndexedContainer.addContainerProperty("Amount", String.class, null);
		mTransactionIndexedContainer.addContainerProperty("Transaction Id", String.class, null);
		mTransactionIndexedContainer.addContainerProperty("Source", String.class, null);
		System.out.println("Adding data : "+ membershipTransactions);
        for (MembershipTransaction oneMembershipTransaction:membershipTransactions) {
        	System.out.println("Adding data : "+oneMembershipTransaction);
            Item item = mTransactionIndexedContainer.addItem(oneMembershipTransaction.getId());
            item.getItemProperty("ID").setValue(oneMembershipTransaction.getId());
            item.getItemProperty("Transaction Date").setValue(oneMembershipTransaction.getTransactionDate());
            item.getItemProperty("Amount").setValue(oneMembershipTransaction.getAmount()+" Rs.");
            item.getItemProperty("Transaction Id").setValue(oneMembershipTransaction.getSourceTransactionId());
            item.getItemProperty("Source").setValue(oneMembershipTransaction.getSource());

        }
		membershipTransactionTable.setContainerDataSource(mTransactionIndexedContainer);
		membershipTransactionTable.setVisibleColumns(new Object[] {
				"Transaction Date",
				"Amount",
				"Transaction Id",
				"Source"});
		
		Panel paymentPanel = new Panel("Membership Payment Details");
		paymentPanel.setContent(membershipTransactionTable);
		paymentPanel.setWidth("600px");
		
		content = new VerticalLayout(errorLabel, successLabel, gridLayout, paymentPanel, feePaymentButton);
		Panel contactPanel = new Panel("Membership Details");
		contactPanel.setContent(content);

		this.addComponent(contactPanel);
	}
	
	private void addListeners(){
		feePaymentButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Page.getCurrent().open("https://www.instamojo.com/SwarajAbhiyan/membership-fee-for-swaraj-abhiyannew/", "Memership Fee");
				
			}
		});
	}

	@Override
	public String getNaviagationName() {
		return MembershipView.NAVIAGATION_NAME;
	}

}
