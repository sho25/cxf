begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|sample
operator|.
name|ws
operator|.
name|service
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Entity
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Id
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Table
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_class
annotation|@
name|Entity
annotation|@
name|Table
argument_list|(
name|name
operator|=
literal|"t_counter"
argument_list|)
specifier|public
class|class
name|FirstCounter
implements|implements
name|Serializable
block|{
specifier|private
name|int
name|id
decl_stmt|;
specifier|private
name|int
name|counter
decl_stmt|;
specifier|public
name|FirstCounter
parameter_list|()
block|{     }
specifier|public
name|FirstCounter
parameter_list|(
name|int
name|id
parameter_list|,
name|int
name|initialCounterValue
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|counter
operator|=
name|initialCounterValue
expr_stmt|;
block|}
annotation|@
name|Id
specifier|public
name|int
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|int
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|int
name|getCounter
parameter_list|()
block|{
return|return
name|counter
return|;
block|}
specifier|public
name|void
name|setCounter
parameter_list|(
name|int
name|counter
parameter_list|)
block|{
name|this
operator|.
name|counter
operator|=
name|counter
expr_stmt|;
block|}
specifier|public
name|void
name|incrementCounter
parameter_list|(
name|int
name|howMany
parameter_list|)
block|{
name|setCounter
argument_list|(
name|getCounter
argument_list|()
operator|+
name|howMany
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
