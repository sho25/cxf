begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|corba
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|corba
operator|.
name|common
operator|.
name|Account
import|;
end_import

begin_import
import|import
name|corba
operator|.
name|common
operator|.
name|AccountHelper
import|;
end_import

begin_import
import|import
name|corba
operator|.
name|common
operator|.
name|Bank
import|;
end_import

begin_import
import|import
name|corba
operator|.
name|common
operator|.
name|BankHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|ORB
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|UserException
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|Client
block|{
specifier|private
name|Client
parameter_list|()
block|{
comment|//not constructed
block|}
specifier|static
name|int
name|run
parameter_list|(
name|ORB
name|orb
parameter_list|,
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|UserException
block|{
comment|// Get the Bank object
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|Object
name|obj
init|=
name|orb
operator|.
name|string_to_object
argument_list|(
literal|"corbaname::localhost:1050#Bank"
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"bank.Client: cannot read IOR from corbaname::localhost:1050#Bank"
argument_list|)
expr_stmt|;
return|return
literal|1
return|;
block|}
name|Bank
name|bank
init|=
name|BankHelper
operator|.
name|narrow
argument_list|(
name|obj
argument_list|)
decl_stmt|;
comment|// Test the method Bank.create_account()
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Creating account called \"Account1\""
argument_list|)
expr_stmt|;
name|Account
name|account1
init|=
name|bank
operator|.
name|create_account
argument_list|(
literal|"Account1"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Depositing 100.00 into account \"Account1\""
argument_list|)
expr_stmt|;
name|account1
operator|.
name|deposit
argument_list|(
literal|100.00f
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Current balance of \"Account1\" is "
operator|+
name|account1
operator|.
name|get_balance
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
comment|// Test the method Bank.create_epr_account()
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Creating account called \"Account2\""
argument_list|)
expr_stmt|;
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|Object
name|account2Obj
init|=
name|bank
operator|.
name|create_epr_account
argument_list|(
literal|"Account2"
argument_list|)
decl_stmt|;
name|Account
name|account2
init|=
name|AccountHelper
operator|.
name|narrow
argument_list|(
name|account2Obj
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Depositing 5.00 into account \"Account2\""
argument_list|)
expr_stmt|;
name|account2
operator|.
name|deposit
argument_list|(
literal|5.00f
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Current balance of \"Account2\" is "
operator|+
name|account2
operator|.
name|get_balance
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
comment|// Create two more accounts to use with the getAccount calls
name|Account
name|acc3
init|=
name|bank
operator|.
name|create_account
argument_list|(
literal|"Account3"
argument_list|)
decl_stmt|;
name|acc3
operator|.
name|deposit
argument_list|(
literal|200.00f
argument_list|)
expr_stmt|;
name|Account
name|acc4
init|=
name|bank
operator|.
name|create_account
argument_list|(
literal|"Account4"
argument_list|)
decl_stmt|;
name|acc4
operator|.
name|deposit
argument_list|(
literal|400.00f
argument_list|)
expr_stmt|;
comment|// Test the method Bank.get_account()
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Retrieving account called \"Account3\""
argument_list|)
expr_stmt|;
name|Account
name|account3
init|=
name|bank
operator|.
name|get_account
argument_list|(
literal|"Account3"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Current balance for \"Account3\" is "
operator|+
name|account3
operator|.
name|get_balance
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Depositing 10.00 into account \"Account3\""
argument_list|)
expr_stmt|;
name|account3
operator|.
name|deposit
argument_list|(
literal|10.00f
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"New balance for account \"Account3\" is "
operator|+
name|account3
operator|.
name|get_balance
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
comment|// Test the method Bank.get_epr_account()
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Retrieving account called \"Account4\""
argument_list|)
expr_stmt|;
name|Account
name|account4
init|=
name|bank
operator|.
name|get_account
argument_list|(
literal|"Account4"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Current balance for \"Account4\" is "
operator|+
name|account4
operator|.
name|get_balance
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Withdrawing 150.00 into account \"Account4\""
argument_list|)
expr_stmt|;
name|account4
operator|.
name|deposit
argument_list|(
operator|-
literal|150.00f
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"New balance for account \"Account4\" is "
operator|+
name|account4
operator|.
name|get_balance
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|bank
operator|.
name|remove_account
argument_list|(
literal|"Account1"
argument_list|)
expr_stmt|;
name|bank
operator|.
name|remove_account
argument_list|(
literal|"Account2"
argument_list|)
expr_stmt|;
name|bank
operator|.
name|remove_account
argument_list|(
literal|"Account3"
argument_list|)
expr_stmt|;
name|bank
operator|.
name|remove_account
argument_list|(
literal|"Account4"
argument_list|)
expr_stmt|;
return|return
literal|0
return|;
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
name|args
index|[]
parameter_list|)
block|{
name|int
name|status
init|=
literal|0
decl_stmt|;
name|ORB
name|orb
init|=
literal|null
decl_stmt|;
name|Properties
name|props
init|=
name|System
operator|.
name|getProperties
argument_list|()
decl_stmt|;
try|try
block|{
name|orb
operator|=
name|ORB
operator|.
name|init
argument_list|(
name|args
argument_list|,
name|props
argument_list|)
expr_stmt|;
name|status
operator|=
name|run
argument_list|(
name|orb
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|status
operator|=
literal|1
expr_stmt|;
block|}
if|if
condition|(
name|orb
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|orb
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|status
operator|=
literal|1
expr_stmt|;
block|}
block|}
name|System
operator|.
name|exit
argument_list|(
name|status
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

