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
name|feature
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
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
name|interceptor
operator|.
name|FIStaxInInterceptor
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
name|interceptor
operator|.
name|FIStaxOutInterceptor
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
name|interceptor
operator|.
name|InterceptorProvider
import|;
end_import

begin_comment
comment|//import org.apache.cxf.interceptor.FIStaxInInterceptor;
end_comment

begin_comment
comment|/**  *<pre>  *<![CDATA[<jaxws:endpoint ...><jaxws:features><bean class="org.apache.cxf.feature.FastInfosetFeature"/></jaxws:features></jaxws:endpoint>   ]]></pre>  */
end_comment

begin_class
specifier|public
class|class
name|FastInfosetFeature
extends|extends
name|AbstractFeature
block|{
name|boolean
name|force
decl_stmt|;
specifier|public
name|FastInfosetFeature
parameter_list|()
block|{
comment|//
block|}
annotation|@
name|Override
specifier|protected
name|void
name|initializeProvider
parameter_list|(
name|InterceptorProvider
name|provider
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|FIStaxInInterceptor
name|in
init|=
operator|new
name|FIStaxInInterceptor
argument_list|()
decl_stmt|;
name|FIStaxOutInterceptor
name|out
init|=
operator|new
name|FIStaxOutInterceptor
argument_list|(
name|force
argument_list|)
decl_stmt|;
name|provider
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set if FastInfoset is always used without negotiation       * @param b      */
specifier|public
name|void
name|setForce
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|force
operator|=
name|b
expr_stmt|;
block|}
comment|/**      * Retrieve the value set with {@link #setForce(int)}.      * @return      */
specifier|public
name|boolean
name|getForce
parameter_list|()
block|{
return|return
name|force
return|;
block|}
block|}
end_class

end_unit

