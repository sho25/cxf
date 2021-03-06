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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|bank
operator|.
name|common
operator|.
name|Account
import|;
end_import

begin_import
import|import
name|bank
operator|.
name|common
operator|.
name|AccountAlreadyExistsException
import|;
end_import

begin_import
import|import
name|bank
operator|.
name|common
operator|.
name|AccountNotFoundException
import|;
end_import

begin_import
import|import
name|bank
operator|.
name|common
operator|.
name|Bank
import|;
end_import

begin_import
import|import
name|bank
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
name|QName
name|SERVICE_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/schemas/cxf/idl/bank"
argument_list|,
literal|"BankCORBAService"
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
name|URL
name|wsdlUrl
init|=
name|Client
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/wsdl/bank.wsdl"
argument_list|)
decl_stmt|;
name|BankCORBAService
name|ss
init|=
operator|new
name|BankCORBAService
argument_list|(
name|wsdlUrl
argument_list|,
name|SERVICE_NAME
argument_list|)
decl_stmt|;
name|Bank
name|port
init|=
name|ss
operator|.
name|getBankCORBAPort
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
literal|"Invoking createAccount for Mr. John... "
argument_list|)
expr_stmt|;
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Holder
argument_list|<
name|Account
argument_list|>
name|account
init|=
operator|new
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Holder
argument_list|<>
argument_list|(
operator|new
name|Account
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|port
operator|.
name|createAccount
argument_list|(
literal|"John"
argument_list|,
name|account
argument_list|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"success"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"failure (Unknown)"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|AccountAlreadyExistsException
name|ex
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"failure ("
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
operator|+
literal|" : "
operator|+
name|ex
operator|.
name|getFaultInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
name|Account
name|bankAccount
init|=
name|account
operator|.
name|value
decl_stmt|;
if|if
condition|(
name|bankAccount
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Created Account : "
operator|+
name|bankAccount
operator|.
name|getName
argument_list|()
operator|+
literal|": "
operator|+
name|bankAccount
operator|.
name|getBalance
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Getting Mr. John's account..."
argument_list|)
expr_stmt|;
try|try
block|{
name|bankAccount
operator|=
name|port
operator|.
name|getAccount
argument_list|(
literal|"John"
argument_list|)
expr_stmt|;
if|if
condition|(
name|bankAccount
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"success"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|bankAccount
operator|.
name|getName
argument_list|()
operator|+
literal|": "
operator|+
name|bankAccount
operator|.
name|getBalance
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"failure"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|AccountNotFoundException
name|ex
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"failure ("
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
operator|+
literal|" : "
operator|+
name|ex
operator|.
name|getFaultInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Getting an non-existent account (Ms. Helen)..."
argument_list|)
expr_stmt|;
try|try
block|{
name|bankAccount
operator|=
name|port
operator|.
name|getAccount
argument_list|(
literal|"Helen"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AccountNotFoundException
name|ex
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Caught the expected AccountNotFoundException("
operator|+
name|ex
operator|.
name|getFaultInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

