begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|cxf
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
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|wsaddressing
operator|.
name|W3CEndpointReference
import|;
end_import

begin_import
import|import
name|cxf
operator|.
name|common
operator|.
name|Account
import|;
end_import

begin_import
import|import
name|cxf
operator|.
name|common
operator|.
name|AccountCORBAService
import|;
end_import

begin_import
import|import
name|cxf
operator|.
name|common
operator|.
name|Bank
import|;
end_import

begin_import
import|import
name|cxf
operator|.
name|common
operator|.
name|BankCORBAService
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|Client
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|Client
operator|.
name|class
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
name|Client
parameter_list|()
block|{     }
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"Resolving the bank object"
argument_list|)
expr_stmt|;
name|BankCORBAService
name|service
init|=
operator|new
name|BankCORBAService
argument_list|()
decl_stmt|;
name|Bank
name|port
init|=
name|service
operator|.
name|getBankCORBAPort
argument_list|()
decl_stmt|;
comment|// Test the method Bank.createAccount()
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Creating account called \"Account1\""
argument_list|)
expr_stmt|;
name|W3CEndpointReference
name|epr1
init|=
name|port
operator|.
name|createAccount
argument_list|(
literal|"Account1"
argument_list|)
decl_stmt|;
name|Account
name|account1
init|=
name|getAccountFromEPR
argument_list|(
name|epr1
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Depositing 100.00 into account \'Account1\""
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
literal|"Current balance of account \"Account1\" is "
operator|+
name|account1
operator|.
name|getBalance
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
comment|/* Re-enable when we have a utility to manipulate the meta data stored            within the EPR.         // Test the method Bank.createEprAccount()         System.out.println("Creating account called \"Account2\"");         W3CEndpointReference epr2 = port.createEprAccount("Account2");         Account account2 = getAccountFromEPR(epr2);         System.out.println("Depositing 5.00 into account \'Account2\"");         account2.deposit(5.00f);         System.out.println("Current balance of account \"Account2\" is " + account2.getBalance());         System.out.println();         */
comment|// create two more accounts to use with the getAccount calls
name|Account
name|acc3
init|=
name|getAccountFromEPR
argument_list|(
name|port
operator|.
name|createAccount
argument_list|(
literal|"Account3"
argument_list|)
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
name|getAccountFromEPR
argument_list|(
name|port
operator|.
name|createAccount
argument_list|(
literal|"Account4"
argument_list|)
argument_list|)
decl_stmt|;
name|acc4
operator|.
name|deposit
argument_list|(
literal|400.00f
argument_list|)
expr_stmt|;
comment|// Test the method Bank.getAccount()
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Retrieving account called \"Account3\""
argument_list|)
expr_stmt|;
name|W3CEndpointReference
name|epr3
init|=
name|port
operator|.
name|getAccount
argument_list|(
literal|"Account3"
argument_list|)
decl_stmt|;
name|Account
name|account3
init|=
name|getAccountFromEPR
argument_list|(
name|epr3
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
name|getBalance
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
name|getBalance
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
comment|/* Re-enable when we have a utility to manipulate the meta data stored            within the EPR.         // Test the method Bank.getEprAccount()         System.out.println("Retrieving account called \"Account4\"");         EndpointReferenceType epr4 = port.getEprAccount("Account4");         Account account4 = getAccountFromEPR(epr4);         System.out.println("Current balance for account \"Account4\" is " + account4.getBalance());         System.out.println("Withdrawing 150.00 into account \"Account4\"");         account4.deposit(-150.00f);         System.out.println("New balance for account \"Account4\" is " + account4.getBalance());         System.out.println();         */
name|port
operator|.
name|removeAccount
argument_list|(
literal|"Account1"
argument_list|)
expr_stmt|;
name|port
operator|.
name|removeAccount
argument_list|(
literal|"Account3"
argument_list|)
expr_stmt|;
name|port
operator|.
name|removeAccount
argument_list|(
literal|"Account4"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|Account
name|getAccountFromEPR
parameter_list|(
name|W3CEndpointReference
name|epr
parameter_list|)
block|{
name|AccountCORBAService
name|service
init|=
operator|new
name|AccountCORBAService
argument_list|()
decl_stmt|;
return|return
name|service
operator|.
name|getPort
argument_list|(
name|epr
argument_list|,
name|Account
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

