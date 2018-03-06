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
name|transport
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * Helper methods for {@link DestinationFactory}s and {@link ConduitInitiator}s.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractTransportFactory
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PREFERRED_TRANSPORT_ID
init|=
literal|"org.apache.cxf.preferred.transport.id"
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|transportIds
decl_stmt|;
specifier|public
name|AbstractTransportFactory
parameter_list|()
block|{     }
specifier|public
name|AbstractTransportFactory
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|ids
parameter_list|)
block|{
name|transportIds
operator|=
name|ids
expr_stmt|;
block|}
specifier|public
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|getTransportIds
parameter_list|()
block|{
return|return
name|transportIds
return|;
block|}
specifier|public
name|void
name|setTransportIds
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|transportIds
parameter_list|)
block|{
name|this
operator|.
name|transportIds
operator|=
name|transportIds
expr_stmt|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getUriPrefixes
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
block|}
end_class

end_unit

