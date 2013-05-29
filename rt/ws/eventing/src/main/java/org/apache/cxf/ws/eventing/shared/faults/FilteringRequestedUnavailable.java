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
name|ws
operator|.
name|eventing
operator|.
name|shared
operator|.
name|faults
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|eventing
operator|.
name|shared
operator|.
name|EventingConstants
import|;
end_import

begin_class
specifier|public
class|class
name|FilteringRequestedUnavailable
extends|extends
name|WSEventingFault
block|{
specifier|public
specifier|static
specifier|final
name|String
name|REASON
init|=
literal|"The requested filter dialect is not supported."
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LOCAL_PART
init|=
literal|"FilteringRequestedUnavailable"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
specifier|public
name|FilteringRequestedUnavailable
parameter_list|()
block|{
name|super
argument_list|(
name|REASON
argument_list|,
literal|null
argument_list|,
operator|new
name|QName
argument_list|(
name|EventingConstants
operator|.
name|EVENTING_2011_03_NAMESPACE
argument_list|,
name|LOCAL_PART
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

