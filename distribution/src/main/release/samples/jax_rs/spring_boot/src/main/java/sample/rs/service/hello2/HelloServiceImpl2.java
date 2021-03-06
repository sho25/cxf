begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|sample
operator|.
name|rs
operator|.
name|service
operator|.
name|hello2
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|sample
operator|.
name|rs
operator|.
name|service
operator|.
name|api
operator|.
name|HelloService
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"/sayHello2"
argument_list|)
specifier|public
class|class
name|HelloServiceImpl2
implements|implements
name|HelloService
block|{
specifier|public
name|String
name|sayHello
parameter_list|(
name|String
name|a
parameter_list|)
block|{
return|return
literal|"Hello2 "
operator|+
name|a
operator|+
literal|", Welcome to CXF RS Spring Boot World!!!"
return|;
block|}
block|}
end_class

end_unit

