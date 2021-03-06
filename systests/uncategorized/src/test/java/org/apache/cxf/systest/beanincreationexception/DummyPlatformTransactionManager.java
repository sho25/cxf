begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|systest
operator|.
name|beanincreationexception
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|PlatformTransactionManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|TransactionDefinition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|TransactionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|TransactionStatus
import|;
end_import

begin_class
specifier|public
class|class
name|DummyPlatformTransactionManager
implements|implements
name|PlatformTransactionManager
block|{
specifier|public
name|void
name|commit
parameter_list|(
name|TransactionStatus
name|arg0
parameter_list|)
throws|throws
name|TransactionException
block|{      }
specifier|public
name|TransactionStatus
name|getTransaction
parameter_list|(
name|TransactionDefinition
name|arg0
parameter_list|)
throws|throws
name|TransactionException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|rollback
parameter_list|(
name|TransactionStatus
name|arg0
parameter_list|)
throws|throws
name|TransactionException
block|{      }
block|}
end_class

end_unit

