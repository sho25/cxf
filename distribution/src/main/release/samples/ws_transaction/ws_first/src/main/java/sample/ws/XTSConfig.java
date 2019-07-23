begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * JBoss, Home of Professional Open Source.  * Copyright 2019, Red Hat, Inc., and individual contributors  * as indicated by the @author tags. See the copyright.txt file in the  * distribution for a full listing of individual contributors.  *  * This is free software; you can redistribute it and/or modify it  * under the terms of the GNU Lesser General Public License as  * published by the Free Software Foundation; either version 2.1 of  * the License, or (at your option) any later version.  *  * This software is distributed in the hope that it will be useful,  * but WITHOUT ANY WARRANTY; without even the implied warranty of  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU  * Lesser General Public License for more details.  *  * You should have received a copy of the GNU Lesser General Public  * License along with this software; if not, write to the Free  * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  * 02110-1301 USA, or see the FSF site: http://www.fsf.org.  */
end_comment

begin_package
package|package
name|sample
operator|.
name|ws
package|;
end_package

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|jbossts
operator|.
name|XTSService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|jbossts
operator|.
name|txbridge
operator|.
name|inbound
operator|.
name|InboundBridgeRecoveryManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|jbossts
operator|.
name|xts
operator|.
name|environment
operator|.
name|WSCEnvironmentBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|jbossts
operator|.
name|xts
operator|.
name|environment
operator|.
name|XTSEnvironmentBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|jbossts
operator|.
name|xts
operator|.
name|environment
operator|.
name|XTSPropertyManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Value
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Bean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|DependsOn
import|;
end_import

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

begin_comment
comment|/**  * @author<a href="mailto:zfeng@redhat.com">Zheng Feng</a>  */
end_comment

begin_class
annotation|@
name|Configuration
specifier|public
class|class
name|XTSConfig
block|{
annotation|@
name|Value
argument_list|(
literal|"${server.port}"
argument_list|)
specifier|private
name|int
name|port
decl_stmt|;
annotation|@
name|Bean
argument_list|(
name|name
operator|=
literal|"xtsService"
argument_list|,
name|initMethod
operator|=
literal|"start"
argument_list|,
name|destroyMethod
operator|=
literal|"stop"
argument_list|)
specifier|public
name|XTSService
name|xtsService
parameter_list|()
block|{
name|WSCEnvironmentBean
name|wscEnvironmentBean
init|=
name|XTSPropertyManager
operator|.
name|getWSCEnvironmentBean
argument_list|()
decl_stmt|;
name|wscEnvironmentBean
operator|.
name|setBindPort11
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|XTSService
name|service
init|=
operator|new
name|XTSService
argument_list|()
decl_stmt|;
return|return
name|service
return|;
block|}
annotation|@
name|Bean
argument_list|(
name|initMethod
operator|=
literal|"start"
argument_list|,
name|destroyMethod
operator|=
literal|"stop"
argument_list|)
annotation|@
name|DependsOn
argument_list|(
block|{
literal|"xtsService"
block|}
argument_list|)
specifier|public
name|InboundBridgeRecoveryManager
name|inboundBridgeRecoveryManager
parameter_list|()
block|{
return|return
operator|new
name|InboundBridgeRecoveryManager
argument_list|()
return|;
block|}
block|}
end_class

end_unit

