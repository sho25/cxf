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
name|tools
operator|.
name|common
operator|.
name|model
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

begin_class
specifier|public
class|class
name|JavaServiceClass
extends|extends
name|JavaClass
block|{
specifier|private
specifier|final
name|List
argument_list|<
name|JavaPort
argument_list|>
name|ports
init|=
operator|new
name|ArrayList
argument_list|<
name|JavaPort
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|String
name|serviceName
decl_stmt|;
specifier|public
name|JavaServiceClass
parameter_list|(
name|JavaModel
name|model
parameter_list|)
block|{
name|super
argument_list|(
name|model
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addPort
parameter_list|(
name|JavaPort
name|port
parameter_list|)
block|{
name|ports
operator|.
name|add
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|JavaPort
argument_list|>
name|getPorts
parameter_list|()
block|{
return|return
name|ports
return|;
block|}
specifier|public
name|void
name|setServiceName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|serviceName
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|String
name|getServiceName
parameter_list|()
block|{
return|return
name|serviceName
return|;
block|}
block|}
end_class

end_unit

