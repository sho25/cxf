begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * JBoss, Home of Professional Open Source.  * Copyright 2019, Red Hat, Inc., and individual contributors  * as indicated by the @author tags. See the copyright.txt file in the  * distribution for a full listing of individual contributors.  *  * This is free software; you can redistribute it and/or modify it  * under the terms of the GNU Lesser General Public License as  * published by the Free Software Foundation; either version 2.1 of  * the License, or (at your option) any later version.  *  * This software is distributed in the hope that it will be useful,  * but WITHOUT ANY WARRANTY; without even the implied warranty of  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU  * Lesser General Public License for more details.  *  * You should have received a copy of the GNU Lesser General Public  * License along with this software; if not, write to the Free  * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  * 02110-1301 USA, or see the FSF site: http://www.fsf.org.  */
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
name|org
operator|.
name|springframework
operator|.
name|data
operator|.
name|repository
operator|.
name|CrudRepository
import|;
end_import

begin_comment
comment|/**  * @author<a href="mailto:zfeng@redhat.com">Zheng Feng</a>  */
end_comment

begin_interface
specifier|public
interface|interface
name|FirstCounterRepository
extends|extends
name|CrudRepository
argument_list|<
name|FirstCounter
argument_list|,
name|Integer
argument_list|>
block|{  }
end_interface

end_unit

