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
name|corba
operator|.
name|common
operator|.
name|idltypes
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|IdlAnonFixed
extends|extends
name|IdlFixedBase
block|{
specifier|private
name|IdlAnonFixed
parameter_list|(
name|IdlScopeBase
name|parent
parameter_list|,
name|int
name|digits
parameter_list|,
name|int
name|scale
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|,
literal|""
argument_list|,
name|digits
argument_list|,
name|scale
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|IdlAnonFixed
name|create
parameter_list|(
name|IdlScopeBase
name|parent
parameter_list|,
name|int
name|digits
parameter_list|,
name|int
name|scale
parameter_list|)
block|{
return|return
operator|new
name|IdlAnonFixed
argument_list|(
name|parent
argument_list|,
name|digits
argument_list|,
name|scale
argument_list|)
return|;
block|}
specifier|public
name|String
name|fullName
parameter_list|()
block|{
return|return
literal|"fixed<"
operator|+
name|digits
argument_list|()
operator|+
literal|", "
operator|+
name|scale
argument_list|()
operator|+
literal|"> "
return|;
block|}
specifier|public
name|String
name|fullName
parameter_list|(
name|IdlScopedName
name|rel
parameter_list|)
block|{
return|return
name|fullName
argument_list|()
return|;
block|}
specifier|public
name|void
name|write
parameter_list|(
name|PrintWriter
name|pw
parameter_list|)
block|{
comment|// intentionally empty
block|}
block|}
end_class

end_unit

