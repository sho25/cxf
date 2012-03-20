begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|corba
operator|.
name|server
package|;
end_package

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|PortableServer
operator|.
name|POA
import|;
end_import

begin_import
import|import
name|corba
operator|.
name|common
operator|.
name|HelloWorldPOA
import|;
end_import

begin_class
specifier|public
class|class
name|HelloWorldImpl
extends|extends
name|HelloWorldPOA
block|{
comment|// The servants default POA
specifier|private
name|POA
name|poa
decl_stmt|;
name|HelloWorldImpl
parameter_list|(
name|POA
name|p
parameter_list|)
block|{
name|poa
operator|=
name|p
expr_stmt|;
block|}
specifier|public
name|String
name|greetMe
parameter_list|(
name|String
name|inparameter
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"In greetMe("
operator|+
name|inparameter
operator|+
literal|")"
argument_list|)
expr_stmt|;
return|return
literal|"Hi "
operator|+
name|inparameter
return|;
block|}
specifier|public
name|POA
name|_default_POA
parameter_list|()
block|{
return|return
name|poa
return|;
block|}
block|}
end_class

end_unit

