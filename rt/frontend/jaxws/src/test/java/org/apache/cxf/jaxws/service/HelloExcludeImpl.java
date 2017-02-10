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
name|jaxws
operator|.
name|service
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|endpointInterface
operator|=
literal|"org.apache.cxf.jaxws.service.HelloExcludeOnInterface"
argument_list|)
specifier|public
class|class
name|HelloExcludeImpl
implements|implements
name|HelloExcludeOnInterface
block|{
specifier|public
name|String
name|sayHi
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|text
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getGreetings
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|strings
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|strings
operator|.
name|add
argument_list|(
literal|"Hello"
argument_list|)
expr_stmt|;
name|strings
operator|.
name|add
argument_list|(
literal|"Bonjour"
argument_list|)
expr_stmt|;
return|return
name|strings
return|;
block|}
specifier|public
name|void
name|sayGoodbye
parameter_list|()
block|{     }
specifier|public
name|String
index|[]
name|getStringArray
parameter_list|(
name|String
index|[]
name|strs
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getStringList
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|list
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|int
name|echoExcluded
parameter_list|(
name|int
name|i
parameter_list|)
block|{
return|return
literal|0
return|;
block|}
block|}
end_class

end_unit

