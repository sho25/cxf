begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|ejb
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|ejb
operator|.
name|MessageDrivenBean
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ejb
operator|.
name|MessageDrivenContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|Greeter
import|;
end_import

begin_class
specifier|public
class|class
name|GreeterBean
implements|implements
name|MessageDrivenBean
implements|,
name|Greeter
block|{
specifier|public
name|String
name|sayHi
parameter_list|()
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"sayHi called "
argument_list|)
expr_stmt|;
return|return
literal|"Hi there!"
return|;
block|}
specifier|public
name|String
name|greetMe
parameter_list|(
name|String
name|user
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"greetMe called user = "
operator|+
name|user
argument_list|)
expr_stmt|;
return|return
literal|"Hello "
operator|+
name|user
return|;
block|}
comment|//---------------- EJB Methods
specifier|public
name|void
name|ejbCreate
parameter_list|()
block|{     }
specifier|public
name|void
name|ejbRemove
parameter_list|()
block|{     }
specifier|public
name|void
name|setMessageDrivenContext
parameter_list|(
name|MessageDrivenContext
name|mdc
parameter_list|)
block|{     }
block|}
end_class

end_unit

